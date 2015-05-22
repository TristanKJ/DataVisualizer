/**
 * This class is intended for later when users will be able
 * to recompile an image as they change the text. It currently is not
 * being used.
 */
package visualizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ImageGUI {

	public ImageGUI(Bitmap bit) throws Exception {
		final Bitmap bitFile = bit;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame editorFrame = new JFrame("Image");
				editorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				editorFrame.setMinimumSize(new Dimension(600, 600));

				BufferedImage image = null;
				try {
					// Component.createImage(500,500);
					image = ImageIO.read(new File(bitFile.fileName));
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
				ImageIcon imageIcon = new ImageIcon(image);
				JLabel label = new JLabel();
				label.setIcon(imageIcon);

				JTextArea metaData = new JTextArea();
				metaData.append(bitFile.getMetaData());

				JTextArea bits = new JTextArea();
				bits.append(Arrays.toString(bitFile.pixelArray));
				bits.setLineWrap(true);
				bits.setColumns(16);

				editorFrame.getContentPane().add(label, BorderLayout.CENTER);
				editorFrame.getContentPane().add(metaData, BorderLayout.NORTH);
				editorFrame.getContentPane().add(bits, BorderLayout.SOUTH);
				editorFrame.pack();
				editorFrame.setLocationRelativeTo(null);
				editorFrame.setVisible(true);
			}
		});
	}
}
