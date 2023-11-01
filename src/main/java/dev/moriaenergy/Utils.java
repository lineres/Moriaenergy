package dev.moriaenergy;

import javax.swing.*;

import dev.moriaenergy.panel.Displayer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Utils {

	public static BufferedImage rotate( BufferedImage image, double angle ) {
		int width = image.getWidth();
		int height = image.getHeight();

		BufferedImage rotated = new BufferedImage(width, height, image.getType());

		Graphics2D g2d = rotated.createGraphics();
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(angle), width / 2f, height / 2f);

		g2d.setTransform(at);
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		return rotated;
	}

	public static void configGirdBagLayout(JPanel main, Displayer displayer, JPanel controller ) {
		GridBagConstraints displayerConstraint = new GridBagConstraints();
		displayerConstraint.fill = GridBagConstraints.BOTH;
		displayerConstraint.gridwidth = 2;
		displayerConstraint.gridheight = 2;
		displayerConstraint.weightx = 1.0;
		displayerConstraint.weighty = 1.0;
		displayerConstraint.gridx = 0;
		displayerConstraint.gridy = 0;
		main.add(displayer, displayerConstraint);

		GridBagConstraints controllerConstraints = new GridBagConstraints();
		controllerConstraints.weighty = 1.0;
		controllerConstraints.fill = GridBagConstraints.VERTICAL;
		controllerConstraints.gridx = 2;
		controllerConstraints.gridy = 0;
		main.add(controller,controllerConstraints);
	}

}
