/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpairing;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

/**
 *
 * @author Shaun
 */
public class SelectAllCellEditor extends DefaultCellEditor
{
    public SelectAllCellEditor(final JTextField textField ) {
        super( textField );
        textField.addFocusListener( new FocusAdapter()
        {
            public void focusGained( final FocusEvent e )
            {
                textField.selectAll();
            }
        } );
    }
}
