package org.chartsy.main.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import org.chartsy.main.ChartFrame;
import org.chartsy.main.chart.Overlay;
import org.chartsy.main.managers.OverlayManager;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.Node;
import org.openide.windows.WindowManager;

public class Overlays extends javax.swing.JDialog
{

    private ChartFrame parent;
    private List<Overlay> initial;
    private List<Overlay> selected;
    private List<Overlay> unselected;

    /** Creates new form Overlays */
    public Overlays(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        parent.setIconImage(WindowManager.getDefault().getMainWindow().getIconImage());
    }

    public void setChartFrame(ChartFrame cf)
    {
        parent = cf;
    }

    public void initForm()
    {
        btnAdd.setEnabled(false);
        btnRemove.setEnabled(false);

        selected = new ArrayList<Overlay>();
        unselected = new ArrayList<Overlay>();

        unselected = OverlayManager.getDefault().getOverlaysList();
        selected = parent.getSplitPanel().getChartPanel().getOverlays();
        initial = selected;

        scrollPane.setEnabled(false);
        scrollPane.setLayout(new BorderLayout());
        scrollPane.setPreferredSize(new Dimension(549, 296));
        scrollPane.setMinimumSize(new Dimension(549, 296));

        lstSelected.setListData(getArray(selected, true));
        lstUnselected.setListData(getArray(unselected, false));
    }

    private void setPanel(Overlay o)
    {
        PropertySheet prop = new PropertySheet();
        prop.setNodes(new Node[]
                {
                    o.getNode()
                });
        Dimension d = prop.getSize();
        prop.setPreferredSize(new Dimension(549, d.height));
        scrollPane.setEnabled(true);
        scrollPane.removeAll();
        scrollPane.add(prop, BorderLayout.CENTER);
        scrollPane.validate();
        validate();
        repaint();
    }

    private String[] getArray(List<Overlay> list, boolean label)
    {
        String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
        {
            array[i] = label == true ? list.get(i).getLabel() : list.get(i).getName();
        }
        return array;
    }

    public 
    @Override
    void paint(Graphics g)
    {
        super.paint(g);
        int index = lstSelected.getSelectedIndex();
        lstSelected.setListData(getArray(selected, true));
        lstSelected.setSelectedIndex(index);
    }

    public 
    @Override
    void update(Graphics g)
    {
        super.update(g);
        repaint();
    }

    public 
    @Override
    void setVisible(boolean b)
    {
        super.setVisible(b);
        if (!b)
        {
            parent.componentFocused();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblIO = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstUnselected = new javax.swing.JList();
        lblSelected = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstSelected = new javax.swing.JList();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        scrollPane = new javax.swing.JPanel();
        lblProperties = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnOk = new javax.swing.JButton();
        btnApply = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(Overlays.class, "Overlays.title")); // NOI18N

        lblIO.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblIO.setText(org.openide.util.NbBundle.getMessage(Overlays.class, "Overlays.lblIO.text")); // NOI18N

        lstUnselected.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstUnselectedMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(lstUnselected);

        lblSelected.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblSelected.setText(org.openide.util.NbBundle.getMessage(Overlays.class, "Overlays.lblSelected.text")); // NOI18N

        lstSelected.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstSelectedMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(lstSelected);

        btnAdd.setText(org.openide.util.NbBundle.getMessage(Overlays.class, "Overlays.btnAdd.text")); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnRemove.setText(org.openide.util.NbBundle.getMessage(Overlays.class, "Overlays.btnRemove.text")); // NOI18N
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        scrollPane.setBackground(new java.awt.Color(255, 255, 255));
        scrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        scrollPane.setAutoscrolls(true);
        scrollPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout scrollPaneLayout = new javax.swing.GroupLayout(scrollPane);
        scrollPane.setLayout(scrollPaneLayout);
        scrollPaneLayout.setHorizontalGroup(
            scrollPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 547, Short.MAX_VALUE)
        );
        scrollPaneLayout.setVerticalGroup(
            scrollPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 294, Short.MAX_VALUE)
        );

        lblProperties.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblProperties.setText(org.openide.util.NbBundle.getMessage(Overlays.class, "Overlays.lblProperties.text")); // NOI18N

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        btnOk.setText(org.openide.util.NbBundle.getMessage(Overlays.class, "Overlays.btnOk.text")); // NOI18N
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnApply.setText(org.openide.util.NbBundle.getMessage(Overlays.class, "Overlays.btnApply.text")); // NOI18N
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        btnCancel.setText(org.openide.util.NbBundle.getMessage(Overlays.class, "Overlays.btnCancel.text")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemove))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblSelected, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblIO, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)))
                .addGap(16, 16, 16)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnOk)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnApply)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnCancel))
                        .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblProperties, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblProperties)
                        .addGap(11, 11, 11)
                        .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCancel)
                            .addComponent(btnApply)
                            .addComponent(btnOk)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblIO)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblSelected)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdd)
                            .addComponent(btnRemove))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lstUnselectedMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lstUnselectedMouseClicked
    {//GEN-HEADEREND:event_lstUnselectedMouseClicked
        switch (evt.getClickCount())
        {
            case 1:
                scrollPane.setEnabled(false);
                btnAdd.setEnabled(true);
                btnRemove.setEnabled(false);
                break;
            case 2:
                btnAdd.doClick();
                break;
        }
}//GEN-LAST:event_lstUnselectedMouseClicked

    private void lstSelectedMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_lstSelectedMouseClicked
    {//GEN-HEADEREND:event_lstSelectedMouseClicked
        switch (evt.getClickCount())
        {
            case 1:
                btnAdd.setEnabled(false);
                if (selected.size() > 0)
                {
                    btnRemove.setEnabled(true);
                } else
                {
                    btnRemove.setEnabled(false);
                }
                int i = lstSelected.getSelectedIndex();
                if (i != -1)
                {
                    setPanel(selected.get(i));
                }
                break;
            case 2:
                btnRemove.doClick();
                break;
        }
}//GEN-LAST:event_lstSelectedMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAddActionPerformed
    {//GEN-HEADEREND:event_btnAddActionPerformed
        int i = lstUnselected.getSelectedIndex();
        if (i != -1)
        {
            Overlay o = unselected.get(i).newInstance();
            selected.add(o);
            lstSelected.setListData(getArray(selected, true));
            setPanel(o);
        }
}//GEN-LAST:event_btnAddActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRemoveActionPerformed
    {//GEN-HEADEREND:event_btnRemoveActionPerformed
        int i = lstSelected.getSelectedIndex();
        if (i != -1)
        {
            selected.remove(i);
            if (selected.isEmpty())
            {
                btnRemove.setEnabled(false);
            }
            scrollPane.setEnabled(false);
            lstSelected.setListData(getArray(selected, true));
            validate();
            repaint();
        }
}//GEN-LAST:event_btnRemoveActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOkActionPerformed
    {//GEN-HEADEREND:event_btnOkActionPerformed
		for (int i = 0; i < initial.size(); i++)
		{
			Overlay overlay = initial.get(i);
			parent.overlayRemoved(overlay);
		}
		for (int i = 0; i < selected.size(); i++)
		{
			Overlay overlay = selected.get(i);
			parent.overlayAdded(overlay);
		}
        setVisible(false);
}//GEN-LAST:event_btnOkActionPerformed

    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnApplyActionPerformed
    {//GEN-HEADEREND:event_btnApplyActionPerformed
        for (int i = 0; i < initial.size(); i++)
		{
			Overlay overlay = initial.get(i);
			parent.overlayRemoved(overlay);
		}
		for (int i = 0; i < selected.size(); i++)
		{
			Overlay overlay = selected.get(i);
			parent.overlayAdded(overlay);
		}
}//GEN-LAST:event_btnApplyActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelActionPerformed
    {//GEN-HEADEREND:event_btnCancelActionPerformed
        selected = initial;
        setVisible(false);
}//GEN-LAST:event_btnCancelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable()
        {

            public void run()
            {
                Overlays dialog = new Overlays(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter()
                {

                    public void windowClosing(java.awt.event.WindowEvent e)
                    {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnRemove;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblIO;
    private javax.swing.JLabel lblProperties;
    private javax.swing.JLabel lblSelected;
    private javax.swing.JList lstSelected;
    private javax.swing.JList lstUnselected;
    private javax.swing.JPanel scrollPane;
    // End of variables declaration//GEN-END:variables
}
