package app;

import org.apache.commons.math3.util.FastMath;
import java.text.DecimalFormat;

public class Population {
	private Zeichnen z;
	private double g, h, s, r, k;
	private int delay;

	public Population(double g, double h, double s, double r, Zeichnen z, int delay) {
		this.g = g;
		this.h = h;
		this.s = s;
		this.r = r;
		this.k = Math.sqrt(s * r);
		this.z = z;
		this.delay = delay;
	}

	public void init() {
		double gH = g / h;
		double hG = h / g;
		DecimalFormat nFormat = new DecimalFormat("0.000");

		if ((s * g * g) - (r * h * h) > 0) {
			double endzeitG = FastMath.atanh(hG * (k / s)) / k;
			System.out.println("Endzeit G: " + nFormat.format(endzeitG));
			print(endzeitG);
		} else if ((s * g * g) - (r * h * h) < 0) {
			double endzeitH = FastMath.atanh(gH * (k / r)) / k;
			System.out.println("Endzeit H: " + nFormat.format(endzeitH));
			print(endzeitH);
		} else {
			double endzeit = FastMath.atanh(gH * (k / r)) / k;
			System.out.println("Endzeit Unentschieden: " + endzeit);
			print(endzeit);
		}
	}

	public void print(double endzeit) {

		DecimalFormat nFormat = new DecimalFormat("0.000");

		double hRest = 0, gRest = 0, hBuffer, gBuffer;

		if ((s * g * g) - (r * h * h) > 0 || (s * g * g) - (r * h * h) < 0) {
			boolean hGewinnt = false;

			if ((s * g * g) - (r * h * h) < 0) {
				hGewinnt = true;
				hRest = Math.round(h * Math.cosh(0) - (s / k) * g * Math.sinh(0));
				gRest = Math.round(g * Math.cosh(0) - (r / k) * h * Math.sinh(0));
			} else if ((s * g * g) - (r * h * h) > 0) {
				hGewinnt = false;
				gRest = Math.round(g * Math.cosh(0) - (r / k) * h * Math.sinh(0));
				hRest = Math.round(h * Math.cosh(0) - (s / k) * g * Math.sinh(0));
			}

			hBuffer = hRest;
			gBuffer = gRest;

			for (double i = 0.000; i <= endzeit; i += 0.0001) {
				if (hGewinnt == true) {
					hRest = Math.round(h * Math.cosh(i * k) - (s / k) * g * Math.sinh(i * k));
					gRest = Math.round(g * Math.cosh(i * k) - (r / k) * h * Math.sinh(i * k));
				} else if (hGewinnt == false) {
					gRest = Math.round(g * Math.cosh(i * k) - (r / k) * h * Math.sinh(i * k));
					hRest = Math.round(h * Math.cosh(i * k) - (s / k) * g * Math.sinh(i * k));
				}

				z.setLab(String.valueOf(endzeit), String.valueOf(gRest), String.valueOf(hRest), String.valueOf(i), String.valueOf(g-gRest), String.valueOf(h-hRest));
				System.out.println("Truppenanzahl G : " + gRest + " zum Zeitpunkt: " + nFormat.format(i));
				System.out.println("Truppenanzahl H : " + hRest + " zum Zeitpunkt: " + nFormat.format(i));

				if (hBuffer >= hRest) {
					double hDifferenz = hBuffer - hRest;
					for (int j = 0; j < hDifferenz; j++)
						z.loescheKreise(true);
					hBuffer = hRest;
				}

				if (gBuffer >= gRest) {
					double gDifferenz = gBuffer - gRest;
					for (int j = 0; j < gDifferenz; j++)
						z.loescheKreise(false);
					gBuffer = gRest;
				}

				z.draw();
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
				}
			}
		} else {
			boolean alleTod = false;
			gRest = Math.round(g * Math.cosh(0) - (r / k) * h * Math.sinh(0));
			hRest = Math.round(h * Math.cosh(0) - (s / k) * g * Math.sinh(0));
			gBuffer = gRest;
			hBuffer = hRest;

			for (double i = 0.000; i<= endzeit && alleTod == false; i += 0.001) {
				gRest = Math.round(g * Math.cosh(i * k) - (r / k) * h * Math.sinh(i * k));
				hRest = Math.round(h * Math.cosh(i * k) - (s / k) * g * Math.sinh(i * k));

				z.setLab(String.valueOf(endzeit), String.valueOf(gRest), String.valueOf(hRest), String.valueOf(i), String.valueOf(g-gRest), String.valueOf(h-hRest));
				System.out.println("Truppenanzahl G : " + gRest + " zum Zeitpunkt: " + nFormat.format(i));
				System.out.println("Truppenanzahl H : " + hRest + " zum Zeitpunkt: " + nFormat.format(i));

				if (gRest == 0 && hRest == 0) {

					System.out.println("G und H haben sich gegenseitig ausgelöscht zum Zeitpunkt: " + nFormat.format(i));

					alleTod = true;
				}

				if (hBuffer > hRest) {
					double hDifferenz = hBuffer - hRest;
					for (int j = 0; j < hDifferenz; j++)
						z.loescheKreise(true);
					hBuffer = hRest;
				}

				if (gBuffer > gRest) {
					double gDifferenz = gBuffer - gRest;
					for (int j = 0; j < gDifferenz; j++)
						z.loescheKreise(false);
					gBuffer = gRest;
				}

				z.draw();

				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public double getH() {
		return h;
	}

	public double getG() {
		return g;
	}
}