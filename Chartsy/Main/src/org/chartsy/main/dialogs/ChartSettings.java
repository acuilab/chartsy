package org.chartsy.main.dialogs;

import org.chartsy.main.ChartFrame;
import org.chartsy.main.chart.Indicator;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.Node;
import org.openide.windows.WindowManager;

/**
 *
 * @author viorel.gheba
 */
public class ChartSettings extends javax.swing.JDialog
{
    
    private ChartFrame chartFrame;

    public ChartSettings(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        parent.setIconImage(WindowManager.getDefault().getMainWindow().getIconImage());
    }

    public void initializeForm(ChartFrame chartFrame)
    {
        this.chartFrame = chartFrame;
        PropertySheet prop = new PropertySheet();
        prop.setMinimumSize(getPreferredSize());
        prop.setMaximumSize(getPreferredSize());
        prop.setPreferredSize(getPreferredSize());
        prop.setNodes(new Node[] {chartFrame.getNode()});
        setContentPane(prop);
    }

    public void forIndicator(ChartFrame chartFrame, Indicator indicator)
    {
        this.chartFrame = chartFrame;
        setTitle(indicator.getName() + " Properties");
        PropertySheet prop = new PropertySheet();
        prop.setNodes(new Node[] {indicator.getNode()});
        setContentPane(prop);
    }

    /*private Node[] getNodes(ChartFrame chartFrame)
    {
        Node[] nodes = new Node[1];
        final AbstractPropertiesNode chartNode = (AbstractPropertiesNode) chartFrame.getNode();
        final List<Overlay> overlays = chartFrame.getMainPanel().getSplitPanel().getChartPanel().getOverlays();

        AbstractPropertiesNode node = new AbstractPropertiesNode("Chart Properties")
        {
            @SuppressWarnings("unchecked")
            protected @Override Sheet createSheet()
            {
                Sheet sheet = new Sheet();

                Sheet.Set[] chartSets = chartNode.getSets();
                for (Sheet.Set chartSet : chartSets)
                {
                    sheet.put(chartSet);
                    chartSet.setPreferred(true);
                }
                
                if (overlays.size() > 0)
                {
                    for (Overlay overlay : overlays)
                    {
                        AbstractPropertiesNode overlayNode = (AbstractPropertiesNode) overlay.getNode();
                        Sheet.Set overlaySet = overlayNode.getSets()[0];
                        overlaySet.setValue("tabName", overlay.getName());
                        sheet.put(overlaySet);
                    }
                }

                return sheet;
            }
        };

        nodes[0] = node;

        return nodes;
    }*/

    public @Override void setVisible(boolean b)
    {
        super.setVisible(b);
        if (!b)
            chartFrame.componentFocused();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(ChartSettings.class, "ChartSettings.title_1")); // NOI18N
        setResizable(false);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 744, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 523, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ChartSettings dialog = new ChartSettings(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public @Override void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
