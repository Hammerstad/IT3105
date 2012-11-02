/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package player.HumanGUIComponent;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author Nicklas
 */
public class Frame extends JFrame {
    private ContentPanel content;
    
    public Frame() {
        super("Quarto");
        Dimension dim = new Dimension(800, 600);
        this.setSize(dim);
        this.setPreferredSize(dim);
        this.content = new ContentPanel();
        this.setContentPane(content);
        this.pack();
        this.setLocationRelativeTo(null);
//        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    public ContentPanel getContent() {
        return this.content;
    }
}
