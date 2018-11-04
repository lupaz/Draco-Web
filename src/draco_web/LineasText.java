
package draco_web;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
 
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.StyleContext;
/**
 *
 * @author luis
 */
public class LineasText extends JPanel{
    
  public JTextPane text_pane;
  public JScrollPane scrollPane;
  public StyleContext sc = new StyleContext();
  public DefaultStyledDocument doc = new DefaultStyledDocument(sc);
  public Highlighter hilite = new Resaltado();
  public ArrayList<Integer> lineasdeb= new ArrayList<>();
  public ArrayList<Integer> lineasdeb2= new ArrayList<>();
 public ArrayList<Object> reflines = new ArrayList<>();
 public ArrayList<Object> reflines2 = new ArrayList<>();
 
  public LineasText (){
    super ();
    setMinimumSize (new Dimension (30, 30));
    setPreferredSize (new Dimension (30, 30));
    text_pane = new JTextPane (){ // se necesita pintar las lineas en el panel 
      public void paint (Graphics g)
      {
	super.paint (g);
	LineasText.this.repaint ();
      }
    };
    text_pane.setDocument(doc);
    text_pane.setHighlighter(hilite);
    //this.text_pane=text_pane;
    scrollPane = new JScrollPane(text_pane);
  }
 
  @Override
  public void paint (Graphics g){
    super.paint (g);
    int start =
      text_pane.viewToModel (scrollPane.getViewport ().getViewPosition ());
    int end =
      text_pane.viewToModel (new
		   Point (scrollPane.getViewport ().getViewPosition ().x +
			  text_pane.getWidth (),
			  scrollPane.getViewport ().getViewPosition ().y +
			  text_pane.getHeight ()));
    Document doc = text_pane.getDocument ();
    int startline = doc.getDefaultRootElement ().getElementIndex (start);
    int endline = doc.getDefaultRootElement ().getElementIndex (end)+1; //pinta la linea numero 1
    int fontHeight = g.getFontMetrics (text_pane.getFont ()).getHeight ();	// fuente
    
    for (int line = startline, y = 0; line <= endline;line++, y += fontHeight){
            g.drawString (Integer.toString (line), 0, y);
      }
  }
  
  public boolean exiteLine(int linea){
      for (int i = 0; i <lineasdeb.size(); i++) {
          if(lineasdeb.get(i)==linea){
              return true;
          }
      }
      return false;
  }
  
  public boolean exiteLine2(int linea){
      for (int i = 0; i <lineasdeb2.size(); i++) {
          if(lineasdeb2.get(i)==linea){
              return true;
          }
      }
      return false;
  }
  
  public Object quitarline(int linea){
      for (int i = 0; i <lineasdeb.size(); i++) {
          if(lineasdeb.get(i)==linea){
              lineasdeb.remove(i);
              Object tmp=reflines.get(i);
              reflines.remove(i);
              return tmp;
          }
      }
      return null;
  }
  
  public Object quitarline2(int linea){
      for (int i = 0; i <lineasdeb2.size(); i++) {
          if(lineasdeb2.get(i)==linea){
              lineasdeb2.remove(i);
              Object tmp=reflines2.get(i);
              reflines2.remove(i);
              return tmp;
          }
      }
      return null;
  }
  
}
