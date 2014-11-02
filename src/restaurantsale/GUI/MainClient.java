/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restaurantsale.GUI;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.table.DefaultTableModel;
import restaurantsale.Tools.MsgBox;
import restaurantsale.Tools.TCPClient;

/**
 *
 * @author rudolf
 */
public class MainClient extends javax.swing.JFrame {

    /**
     * Creates new form MainMenu
     */
    public MainClient() {
        initComponents();
    }

    public MainClient(final String TABLE_NO, String SERVER_IP) {
        this();
        this.TABLE_NO = TABLE_NO;
        this.tcpClient = new TCPClient(SERVER_IP);

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QueueReceiver(6788).start();

                labTable.setText(labTable.getText() + TABLE_NO);
                updateQueueTable(tcpClient.sendMessage("QUE"));
                setMenuTable(tcpClient.sendMessage("MNU"));
                checkOrdered(tcpClient.sendMessage("CHK;" + TABLE_NO));
            }
        });
    }

    // Custom variables
    private String TABLE_NO;
    private TCPClient tcpClient;

    // Custom method
    private void setMenuTable(String format) {
//        System.out.println(format + ":msg");
        if(format.equals("")) return;
        String[] menu = format.split(";"), line, part;
        DefaultTableModel model;

        model = (DefaultTableModel) tabFood.getModel();
        line = menu[0].split(":");
        for(int i=0; i<line.length; ++i){
            part = line[i].split("\\.");
            model.addRow(new Object[]{part[0], part[1]});
        }

        model = (DefaultTableModel) tabBev.getModel();
        line = menu[1].split(":");
        for(int i=0; i<line.length; ++i){
            part = line[i].split("\\.");
            model.addRow(new Object[]{part[0], part[1]});
        }
    }

    private String convertString(int value) {
        String target = "" + value, result = "";
        int j = 0;
        for(int i=target.length()-1; i>=0; --i){
            if(j == 3){
                result += ".";
                j -= 3;
            }
            result += target.charAt(i);
            ++j;
        }
        return new StringBuilder(result).reverse().toString();
    }

    private void confirmOrders(){
        // confirm orders
        if(!MsgBox.ask("Confirm current orders ?\n(selected menu's pricing details is shown in table below)"))
            return;

        this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        String ord = "ORD;" + TABLE_NO + ";";
        for(int i=0; i<tabOrder.getRowCount(); ++i){
            if(i > 0) ord += ";";
            ord += tabOrder.getValueAt(i, 1).toString() + "."
                    + tabOrder.getValueAt(i, 0).toString();
        }

//        System.out.println(ord);
        tcpClient.sendMessage(ord);
        MsgBox.info("Orders successfully sent to Server\n" +
                "Please wait around 15 minutes before the orders are ready", "CONFIRM SUCCESS");

        clearMenuQty();
        checkOrdered(tcpClient.sendMessage("CHK;" + TABLE_NO));
        this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
    }

    private synchronized void updateQueueTable(String format) {
        DefaultTableModel model = (DefaultTableModel) tabQueue.getModel();
        while(model.getRowCount() > 0) model.removeRow(0);

        if(format.equals("")) return;
        String[] line = format.split(":"), part;
        for(int i=0; i<line.length; ++i){
            part = line[i].split("\\.");
            model.addRow(new Object[]{part[0], part[1], part[2]});
        }
    }

    private void checkOrdered(String format) {
        DefaultTableModel model = (DefaultTableModel) tabOrder.getModel();
        while(model.getRowCount() > 0) model.removeRow(0);

        if(format.equals("")) return;
        String[] line = format.split(":"), part;
        int PRICE = 0, TOTAL = 0;
        for(int i=0; i<line.length; ++i){
            part = line[i].split("\\.");
            for(int j=0; j<tabFood.getRowCount(); ++j)
                if(part[0].equals(tabFood.getValueAt(j, 0))){
                    PRICE = Integer.parseInt(tabFood.getValueAt(j, 1).toString());
                    break;
                }

            for(int j=0; j<tabBev.getRowCount(); ++j)
                if(part[0].equals(tabBev.getValueAt(j, 0))){
                    PRICE = Integer.parseInt(tabBev.getValueAt(j, 1).toString());
                    break;
                }

            model.addRow(new Object[]{part[0], part[2], PRICE, PRICE * Integer.parseInt(part[2])});
            TOTAL += PRICE * Integer.parseInt(part[2]);
        }

        labPrice.setText(convertString(TOTAL));
    }

    private void clearMenuQty() {
        DefaultTableModel model;
        model = (DefaultTableModel) tabFood.getModel();
        for(int i=0; i<tabFood.getRowCount(); ++i){
            model.setValueAt("", i, 2);
        }

        model = (DefaultTableModel) tabBev.getModel();
        for(int i=0; i<tabBev.getRowCount(); ++i){
            model.setValueAt("", i, 2);
        }
    }

    public void initNewClient() {
        tcpClient.sendMessage("FIN;" + TABLE_NO);

        DefaultTableModel model;
        model = (DefaultTableModel) tabOrder.getModel();
        while(model.getRowCount() > 0) model.removeRow(0);

        labPrice.setText("0");
    }

    // Custom classes
    private class QueueReceiver extends Thread {
        DataInputStream in;
        Socket socket;
        ServerSocket listenSocket;
        int PORT;

        public QueueReceiver(int PORT) {
            this.PORT = PORT;
        }

        @Override
        public void run() {
            try{
                listenSocket = new ServerSocket(PORT);

                while(true) {
                    socket = listenSocket.accept();
                    in = new DataInputStream(socket.getInputStream());

                    String message = in.readUTF();
//                    System.out.println("Queue receiver:" + message);
                    updateQueueTable(message);
                }
            } catch(IOException e) {
                System.out.println("Listen: " + e.getMessage());
            } finally {
                try {
                    if(socket!=null)
                        socket.close();
                } catch (IOException e) {}
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labTable = new javax.swing.JLabel();
        paneQueue = new javax.swing.JPanel();
        spQueue = new javax.swing.JScrollPane();
        tabQueue = new javax.swing.JTable();
        paneOrder = new javax.swing.JPanel();
        spOrder = new javax.swing.JScrollPane();
        tabOrder = new javax.swing.JTable();
        labTotal = new javax.swing.JLabel();
        labPrice = new javax.swing.JLabel();
        paneMenu = new javax.swing.JPanel();
        spFood = new javax.swing.JScrollPane();
        tabFood = new javax.swing.JTable();
        spBev = new javax.swing.JScrollPane();
        tabBev = new javax.swing.JTable();
        btnDone = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        btnPay = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(6);

        labTable.setFont(new java.awt.Font("DejaVu Sans", 1, 36)); // NOI18N
        labTable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labTable.setText("TABLE : ");

        paneQueue.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ORDERS QUEUE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 255)));

        tabQueue.setFont(new java.awt.Font("DejaVu Sans", 0, 24)); // NOI18N
        tabQueue.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NAME", "TABLE NO", "QUANTITY"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabQueue.setEnabled(false);
        tabQueue.setRowHeight(30);
        tabQueue.setRowSelectionAllowed(false);
        tabQueue.getTableHeader().setReorderingAllowed(false);
        spQueue.setViewportView(tabQueue);
        if (tabQueue.getColumnModel().getColumnCount() > 0) {
            tabQueue.getColumnModel().getColumn(0).setResizable(false);
            tabQueue.getColumnModel().getColumn(0).setPreferredWidth(300);
            tabQueue.getColumnModel().getColumn(1).setResizable(false);
            tabQueue.getColumnModel().getColumn(2).setResizable(false);
        }

        javax.swing.GroupLayout paneQueueLayout = new javax.swing.GroupLayout(paneQueue);
        paneQueue.setLayout(paneQueueLayout);
        paneQueueLayout.setHorizontalGroup(
            paneQueueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneQueueLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spQueue)
                .addContainerGap())
        );
        paneQueueLayout.setVerticalGroup(
            paneQueueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spQueue, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        paneOrder.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "YOUR ORDER", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 255)));

        tabOrder.setFont(new java.awt.Font("DejaVu Sans", 0, 22)); // NOI18N
        tabOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ORDER NAME", "QTY", "@ PRICE", "SUB TOTAL"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabOrder.setEnabled(false);
        tabOrder.setRowHeight(28);
        tabOrder.setRowSelectionAllowed(false);
        tabOrder.getTableHeader().setReorderingAllowed(false);
        spOrder.setViewportView(tabOrder);
        if (tabOrder.getColumnModel().getColumnCount() > 0) {
            tabOrder.getColumnModel().getColumn(0).setResizable(false);
            tabOrder.getColumnModel().getColumn(0).setPreferredWidth(250);
            tabOrder.getColumnModel().getColumn(1).setResizable(false);
            tabOrder.getColumnModel().getColumn(1).setPreferredWidth(30);
            tabOrder.getColumnModel().getColumn(2).setResizable(false);
            tabOrder.getColumnModel().getColumn(2).setPreferredWidth(70);
            tabOrder.getColumnModel().getColumn(3).setResizable(false);
            tabOrder.getColumnModel().getColumn(3).setPreferredWidth(70);
        }

        labTotal.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        labTotal.setText("TOTAL Rp.");

        labPrice.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        labPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labPrice.setText("0");

        javax.swing.GroupLayout paneOrderLayout = new javax.swing.GroupLayout(paneOrder);
        paneOrder.setLayout(paneOrderLayout);
        paneOrderLayout.setHorizontalGroup(
            paneOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneOrderLayout.createSequentialGroup()
                .addGroup(paneOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneOrderLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(labTotal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paneOrderLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(spOrder, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)))
                .addContainerGap())
        );
        paneOrderLayout.setVerticalGroup(
            paneOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneOrderLayout.createSequentialGroup()
                .addComponent(spOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labTotal)
                    .addComponent(labPrice)))
        );

        paneMenu.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PLACE ORDER HERE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 255)));

        tabFood.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        tabFood.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "FOOD", "PRICE", "QUANTITY"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabFood.setRowHeight(24);
        tabFood.getTableHeader().setReorderingAllowed(false);
        tabFood.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tabFoodFocusLost(evt);
            }
        });
        spFood.setViewportView(tabFood);
        tabFood.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        tabBev.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        tabBev.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BEVERAGE", "PRICE", "QUANTITY"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabBev.setRowHeight(24);
        tabBev.getTableHeader().setReorderingAllowed(false);
        tabBev.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tabBevFocusLost(evt);
            }
        });
        spBev.setViewportView(tabBev);

        javax.swing.GroupLayout paneMenuLayout = new javax.swing.GroupLayout(paneMenu);
        paneMenu.setLayout(paneMenuLayout);
        paneMenuLayout.setHorizontalGroup(
            paneMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(spBev, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(spFood, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        paneMenuLayout.setVerticalGroup(
            paneMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneMenuLayout.createSequentialGroup()
                .addComponent(spFood, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spBev, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
        );

        btnDone.setBackground(new java.awt.Color(204, 204, 0));
        btnDone.setFont(new java.awt.Font("DejaVu Sans", 1, 30)); // NOI18N
        btnDone.setText("CONFIRM AND ORDER");
        btnDone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoneActionPerformed(evt);
            }
        });

        btnSearch.setBackground(new java.awt.Color(0, 204, 204));
        btnSearch.setFont(new java.awt.Font("DejaVu Sans", 1, 36)); // NOI18N
        btnSearch.setText("SEARCH");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnPay.setBackground(new java.awt.Color(0, 153, 51));
        btnPay.setFont(new java.awt.Font("DejaVu Sans", 1, 36)); // NOI18N
        btnPay.setText("PAY");
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(paneOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(paneQueue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(paneMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPay, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                            .addComponent(btnDone, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labTable, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paneQueue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(paneMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(paneOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnDone, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(btnPay, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        // show paymentForm
        new PaymentForm(this).setVisible(true);
    }//GEN-LAST:event_btnPayActionPerformed

    private void tabFoodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabFoodFocusLost
        // table focus lost
        if(tabFood.isEditing()) tabFood.getCellEditor().stopCellEditing();
        tabFood.clearSelection();
    }//GEN-LAST:event_tabFoodFocusLost

    private void tabBevFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabBevFocusLost
        // table focus lost
        if(tabBev.isEditing()) tabBev.getCellEditor().stopCellEditing();
        tabBev.clearSelection();
    }//GEN-LAST:event_tabBevFocusLost

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // show search form
        new SearchForm(this.tcpClient).setVisible(true);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnDoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoneActionPerformed
        // simulate selected food and its price (show in tabOrder)
        int QTY, PRICE, TOTAL = 0;
        String NAME;
        DefaultTableModel model = (DefaultTableModel) tabOrder.getModel();
        while(model.getRowCount() > 0) model.removeRow(0);

        for(int i=0; i<tabBev.getRowCount(); ++i){
            QTY = 0;
            NAME = tabBev.getValueAt(i, 0).toString();
            PRICE = Integer.parseInt(tabBev.getValueAt(i, 1).toString());
            try {
                QTY = Integer.parseInt(tabBev.getValueAt(i, 2).toString());
            } catch (Exception e){}

            if(QTY > 0) model.addRow(new Object[]{NAME, QTY, PRICE, PRICE*QTY});
        }

        for(int i=0; i<tabFood.getRowCount(); ++i){
            QTY = 0;
            NAME = tabFood.getValueAt(i, 0).toString();
            PRICE = Integer.parseInt(tabFood.getValueAt(i, 1).toString());
            try {
                QTY = Integer.parseInt(tabFood.getValueAt(i, 2).toString());
            } catch (Exception e){}

            if(QTY > 0) model.addRow(new Object[]{NAME, QTY, PRICE, PRICE*QTY});
        }

        if(model.getRowCount() == 0) return;
        for(int i=0; i<tabOrder.getRowCount(); ++i)
            TOTAL += Integer.parseInt(tabOrder.getValueAt(i, 3).toString());
        labPrice.setText(convertString(TOTAL));
        // ask for order confirmation
        confirmOrders();
    }//GEN-LAST:event_btnDoneActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainClient("B05","127.0.0.1").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDone;
    private javax.swing.JButton btnPay;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel labPrice;
    private javax.swing.JLabel labTable;
    private javax.swing.JLabel labTotal;
    private javax.swing.JPanel paneMenu;
    private javax.swing.JPanel paneOrder;
    private javax.swing.JPanel paneQueue;
    private javax.swing.JScrollPane spBev;
    private javax.swing.JScrollPane spFood;
    private javax.swing.JScrollPane spOrder;
    private javax.swing.JScrollPane spQueue;
    private javax.swing.JTable tabBev;
    private javax.swing.JTable tabFood;
    private javax.swing.JTable tabOrder;
    private javax.swing.JTable tabQueue;
    // End of variables declaration//GEN-END:variables
}
