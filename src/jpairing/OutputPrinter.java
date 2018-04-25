/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpairing;
import java.awt.print.*;
import java.awt.*;

/**
 *
 * @author Shaun
 */
public class OutputPrinter implements Printable{
    
    String[] print_text;
    int[] pageBreaks;

    public OutputPrinter(String[] print_text) {
        this.print_text = print_text;
    }
    
    public void set_text(String[] input_text) {
        print_text = input_text;
    }
    public int print(Graphics g, PageFormat pf, int page)
      throws PrinterException {
        
        // Calculate font size for printing
        // Assume a4 paper 80 chars
        double page_width = pf.getWidth()*1.5;
        int max_width = 1;
        for (String this_line:print_text) {
            if (this_line.length() > max_width)
                max_width = this_line.length();
        }
        int font_size = (int)(page_width/max_width);
        if (font_size > 10)
            font_size = 10;
        
        Font font = new Font("Monospaced", Font.PLAIN, font_size);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int lineHeight = metrics.getHeight();
 
        if (pageBreaks == null) {
            int linesPerPage = (int)(pf.getImageableHeight()/lineHeight);
            int numBreaks = (print_text.length-1)/linesPerPage;
            pageBreaks = new int[numBreaks];
            for (int b=0; b<numBreaks; b++) {
                pageBreaks[b] = (b+1)*linesPerPage; 
            }
        }
 
        if (page > pageBreaks.length) {
            return NO_SUCH_PAGE;
        }
 
        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping
         * Since we are drawing text we
         */
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
 
        /* Draw each line that is on this page.
         * Increment 'y' position by lineHeight for each line.
         */
        int y = 0; 
        int start = (page == 0) ? 0 : pageBreaks[page-1];
        int end   = (page == pageBreaks.length)
                         ? print_text.length : pageBreaks[page];
        for (int line=start; line<end; line++) {
            y += lineHeight;
            g.drawString(print_text[line], 0, y);
        }
 
        /* tell the caller that this page is part of the printed document */
        return PAGE_EXISTS;
    }
    
}
