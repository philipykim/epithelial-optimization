import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGradient;
import org.apache.commons.math3.optim.nonlinear.scalar.gradient.NonLinearConjugateGradientOptimizer;


public class World {

	List<Cell> cells;
	List<Edge> edges;
	List<Vertex> vertices;
	
	PointValuePair optimal;
	
	//For linear interpolation between start and end state
	//I figure we can show the transition betweent the start and end states
	double[] initialState;
	int maxSteps = 50;
	int stepCount = 0;

	private class EnergyFunction implements MultivariateFunction {
		@Override
		public double value(double[] points) {
			return computeEnergyFunctionParametrized(points);
		}
	}
	
	private class EnergyFunctionGradient implements MultivariateVectorFunction {
		@Override
		public double[] value(double[] points) throws IllegalArgumentException {
			double[] gradient = new double[vertices.size()*2];
			// Make a copy of the coordinates
//			double[] twiddle = new double[vertices.size() * 2];
//
//			for (int index = 0; index < vertices.size(); index++) {
//				twiddle[2 * index] = vertices.get(index).x;
//				twiddle[2 * index + 1] = vertices.get(index).y;
//			}
			double[] twiddle = Arrays.copyOf(points, points.length);

			double maxSlope = 0;

			// Twiddle each dimension up and down and record the slope of the energy function
			for (int index = 0; index < twiddle.length; index++) {
				// Up
				twiddle[index] += Constants.epsilon;
				double upEnergy = computeEnergyFunctionParametrized(twiddle);

				// Restore
				twiddle[index] = points[index];

				// Down
				twiddle[index] -= Constants.epsilon;
				double downEnergy = computeEnergyFunctionParametrized(twiddle);

				// Restore
				twiddle[index] = points[index];

				// Partial derivative
				double slope = (upEnergy - downEnergy) / (2 * Constants.epsilon);
				maxSlope = Math.max(maxSlope, slope);
				gradient[index] = slope;
			}
			return gradient;
		}
	}

	public World() {
		cells = new ArrayList<Cell>();
		edges = new ArrayList<Edge>();
		vertices = new ArrayList<Vertex>();
	}
	
	double[] getPoints() {
		double[] points = new double[vertices.size() * 2];
		for (int i = 0; i < vertices.size(); i++) {
			points[2 * i] = vertices.get(i).x;
			points[2*i+1] = vertices.get(i).y;
		}
		return points;
	}

	public void add(Cell c) {
		if (!cells.contains(c)) {
			cells.add(c);
			Vertex last = c.vertices.get(c.vertices.size() - 1);
			for (int i = 0; i < c.vertices.size(); i++) {
				Vertex v = c.vertices.get(i);
				if (!vertices.contains(v)) {
					v.id = vertices.size();
					vertices.add(v);
				}
				Edge e = new Edge(v, last);
				if (!edges.contains(e)) {
					e.id = edges.size();
					edges.add(e);
				}
				last = v;
			}
		}
	}

	public void drawTo(Graphics2D gfx) {
		gfx.setColor(Color.BLACK);
		for (Vertex v: vertices) {
			v.drawTo(gfx);
		}
		for (Edge e : edges) {
			e.drawTo(gfx);
		}
	}

	public double computeEnergyFunction() {
		double energy = 0;
		for (Cell c : cells) {
			energy += c.getEnergy();
		}
		for (Edge e : edges) {
			energy += e.getEnergy();
		}
		return energy;
	}

	public double computeEnergyFunctionParametrized(double[] v) {
		double energy = 0;
		for (Cell c : cells) {
			energy += c.getEnergy(v);
		}
		for (Edge e : edges) {
			energy += e.getEnergy(v);
		}
		return energy;
	}
	
	public void optimize() {
		EnergyFunction e = new EnergyFunction();
		ConvergenceChecker<PointValuePair> cc = new ConvergenceChecker<PointValuePair>() {
			@Override
			public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
				
				final double p = previous.getValue();
				final double c = current.getValue();
				final double difference = Math.abs(p - c);
				final double size = Math.max(Math.abs(p), Math.abs(c));
				return difference <= size * 1e-8 ||
				    difference <= 1e-8;
			}
		};
		ConvergenceChecker<PointValuePair> spc = new SimpleValueChecker(1e-6, 1e-6);
		initialState = getPoints();
		InitialGuess initGuess = new InitialGuess(initialState);
		MultivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(NonLinearConjugateGradientOptimizer.Formula.POLAK_RIBIERE, cc);
		PointValuePair optim = optimizer.optimize(new MaxEval(5000), GoalType.MINIMIZE, initGuess, new ObjectiveFunction(e), new ObjectiveFunctionGradient(new EnergyFunctionGradient()));
		System.out.println("Optimal energy = " + optim.getValue());
		double[] v = optim.getPoint();
		optimal = optim;
	}

	public void step() {
		if (stepCount < maxSteps) {
			double[] points = Arrays.copyOf(initialState, initialState.length);
			double[] finalPoints = Arrays.copyOf(optimal.getPoint(), points.length);
			double[] currentPoint = new double[points.length];
			double t = (double)stepCount / maxSteps;
			for (int i = 0; i < vertices.size(); i++) {
				points[2*i] *= (1.0-t);
				finalPoints[2*i] *= t;
				points[2*i+1] *= (1.0-t);
				finalPoints[2*i+1] *= t;
				currentPoint[2*i] = points[2*i] + finalPoints[2*i];
				currentPoint[2*i+1] = points[2*i+1] + finalPoints[2*i+1];
				vertices.get(i).setLocation(new Point2D.Double(currentPoint[2*i], currentPoint[2*i+1]));
			}
			stepCount++;
		}
//		double[] gradient = new double[vertices.size() * 2];
//
//		// Make a copy of the coordinates
//		double[] twiddle = new double[vertices.size() * 2];
//
//		for (int index = 0; index < vertices.size(); index++) {
//			twiddle[2 * index] = vertices.get(index).x;
//			twiddle[2 * index + 1] = vertices.get(index).y;
//		}
//
//		double maxSlope = 0;
//
//		// Twiddle each dimension up and down and record the slope of the energy function
//		for (int index = 0; index < twiddle.length; index++) {
//			// Up
//			twiddle[index] += Constants.epsilon;
//			double upEnergy = computeEnergyFunctionParametrized(twiddle);
//
//			// Restore
//			Vertex vertex = vertices.get(index / 2);
//			twiddle[index] = index % 2 == 0 ? vertex.x : vertex.y;
//
//			// Down
//			twiddle[index] -= Constants.epsilon;
//			double downEnergy = computeEnergyFunctionParametrized(twiddle);
//
//			// Restore
//			twiddle[index] = index % 2 == 0 ? vertex.x : vertex.y;
//
//			// Partial derivative
//			double slope = (upEnergy - downEnergy) / (2 * Constants.epsilon);
//			maxSlope = Math.max(maxSlope, slope);
//			gradient[index] = slope;
//		}
//
//		for (int index = 0; index < twiddle.length; index++) {
//			twiddle[index] -= gradient[index] / maxSlope * 1.0; // step size = 1
//		}
//
//		update(twiddle);
	}

	public void randomWalkStep() {
//		System.out.println("A = " + cells.get(1).getArea() + "; pref = " +  cells.get(1).preferredArea);

		int triesLeft = 1;
		double lowestEnergy = Double.MAX_VALUE;
		double[] bestConfiguration = null;

		double[] next = new double[vertices.size() * 2];
		while (triesLeft-- > 0) {
			double length = 0;
			for (int i = 0; i < next.length; i++) {
				next[i] = Math.random() - .5;
				length += next[i] * next[i];
			}
			for (int i = 0; i < next.length; i++) {
				next[i] /= length;
			}
			for (int j = 0; j < vertices.size(); j++) {
				next[2 * j] = next[2 * j] / length + vertices.get(j).x;
				next[2 * j + 1] = next[2 * j + 1] / length + vertices.get(j).y;
			}

			double energy = computeEnergyFunctionParametrized(next);
			if (energy < lowestEnergy) {
				lowestEnergy = energy;
				bestConfiguration = next.clone();
				triesLeft = 1000;
			}
		}

		update(bestConfiguration);
	}

	public void update(double[] v) {
//		EnergyFunction e = new EnergyFunction();
//		ConvergenceChecker<PointValuePair> spc = new SimpleValueChecker(1e-13, 1e-13);
//		InitialGuess initGuess = new InitialGuess(getPoints());
//		MultivariateOptimizer optimizer = new NonLinearConjugateGradientOptimizer(NonLinearConjugateGradientOptimizer.Formula.FLETCHER_REEVES, spc);
//		PointValuePair optim = optimizer.optimize(new MaxEval(200), GoalType.MINIMIZE, initGuess, new ObjectiveFunction(e), new ObjectiveFunctionGradient(new EnergyFunctionGradient()));
//		System.out.println(optim.getValue());
//		
//		v = optim.getPoint();
//		for (int i = 0; i < vertices.size(); i++) {
//			vertices.get(i).setLocation(new Point2D.Double(v[2 * i], v[2 * i + 1]));
//		}
	}

}
