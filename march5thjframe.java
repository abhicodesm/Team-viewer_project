
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

public class march5thjframe extends javax.swing.JFrame {

    ArrayList<String> al = new ArrayList<>();
    Mytable tm = new Mytable();
    JLabel lb;
    DataInputStream Dis;
    DataOutputStream Dos;
    int xd, yd, xm, ym;

    public march5thjframe() {
        initComponents();

        setVisible(true);
        jTable1.setModel(tm);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Click");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton2.setText("connect");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(128, 128, 128)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(jButton2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jButton2)
                .addContainerGap(76, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        Thread tt = new Thread(new checkconnection());
        tt.start();


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int index = jTable1.getSelectedRow();
        String ipp=al.get(index);
        buttonconnect bc = new buttonconnect(ipp);
        Thread td = new Thread(bc);
        td.start();
        new file_client(ipp);


    }//GEN-LAST:event_jButton2ActionPerformed

    public static void main(String args[]) {
        march5thjframe obj = new march5thjframe();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    class ping extends Thread {

        Socket sock;
        String k;

        ping(String ip) {
            k = ip;
        }

        public void run() {
            Process p;
            boolean flag = false;
            try {
                p = Runtime.getRuntime().exec("ping " + k);
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String s1 = "";
                while (true) {
                    s1 = br.readLine();

                    if (s1 != null) {
                        s1 = s1.trim();
                        if (s1.equals("")) {

                        } else if (s1.contains("ttl") || s1.contains("TTL")) {
                            sock = new Socket(k, 9000);
                            flag = true;
                            break;
                        } else if (s1.contains("Destination host unreachable") || s1.contains("Request timed out")) {
                            System.out.println("not reachable"+k);
                            break;
                        }
                    }
                }
                if (flag == true) {
                    DataInputStream dis = new DataInputStream(sock.getInputStream());
                    DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
                    dos.writeBytes("hello client\r\n");
                    String d = "hello server";
                    String r = dis.readLine();

                    if (r.equals(d)) {
                        al.add(k);
                        tm.fireTableDataChanged();
                        System.out.println(r);
                    } else {

                    }
                }
            } catch (Exception e) {
            }

        }

    }

    class checkconnection implements Runnable {

        @Override
        public void run() {
            for (int j = 0; j < 17; j++) {
                Thread t[] = new Thread[15];
                for (int i = 0; i < 15; i++) {
                    ping obj = new ping("172.16.5." + (j * 15 + i + 1));
                    t[i] = new Thread(obj);
                    t[i].start();

                }
                for (int k = 0; k < 15; k++) {
                    try {
                        t[k].join();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(march5thjframe.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println("loop completed");
            }

        }

    }

    class Mytable extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return al.size();

        }

        @Override
        public int getColumnCount() {
            return 2;

        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return rowIndex + 1;
            } else {
                return al.get(rowIndex);
            }
        }

    }

    class buttonconnect implements Runnable {

        Socket soc;
        String ipe;

        public buttonconnect(String ipp) {
            ipe = ipp;
        }

        @Override
        public void run() {
            try {
                soc = new Socket(ipe, 9000);
                Dis = new DataInputStream(soc.getInputStream());
                Dos = new DataOutputStream(soc.getOutputStream());
                Dos.writeBytes("Send Screen\r\n");

                while (true) {
                    String msg = Dis.readLine();

                    if (msg.equals("request recieved")) {
                        System.out.println(msg);
                        int sw = Integer.parseInt(Dis.readLine());
                        int sh = Integer.parseInt(Dis.readLine());
                        screendm screen = new screendm(sw, sh);
                        screen.setSize(sw, sh);
                        screen.setVisible(true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class screendm extends javax.swing.JFrame implements MouseListener,MouseMotionListener {

        public screendm(int sw, int sh) {
            setLayout(null);
            lb = new JLabel();
            lb.setBounds(0, 0, sw, sh);
            lb.addMouseListener(this);
            lb.addMouseMotionListener(this);
            add(lb);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            try 
            {
                Dos.writeBytes("MouseDragged\r\n");

                xd = e.getX();
                yd = e.getY();
                Dos.writeByte(xd);
                Dos.writeByte(yd);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void mouseMoved(MouseEvent e) {

            try {
                Dos.writeBytes("MouseMoved\r\n");
                xm = e.getX();
                ym = e.getY();
                Dos.writeByte(xm);
                Dos.writeByte(ym);

            } catch (IOException ex) {
                Logger.getLogger(march5thjframe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            try 
            {
                System.out.println("Mouse clicked");
                Dos.writeBytes("MouseClicked\r\n");
                int x = e.getX();
                int y = e.getY();
                int bt = e.getButton();
                Dos.writeInt(x);
                Dos.writeInt(y);
                Dos.writeInt(bt);
            } catch (IOException exx) {
                exx.printStackTrace();
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {
            try {
                System.out.println("Pressed");
                Dos.writeBytes("MousePressed\r\n");
                int xx = e.getX();
                int yy = e.getY();
                int btt = e.getButton();
                Dos.writeInt(xx);
                Dos.writeInt(yy);
                Dos.writeInt(btt);
            } catch (IOException exp) {
                exp.printStackTrace();
            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            try 
            {
                System.out.println("Released");
                Dos.writeBytes("MouseReleased\r\n");
                int xx = e.getX();
                int yy = e.getY();
                int btt = e.getButton();
                Dos.writeInt(xx);
                Dos.writeInt(yy);
                Dos.writeInt(btt);
            } catch (IOException exp) {
                exp.printStackTrace();
            }

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    public class file_client {

        String ipo;

        public file_client(String ip) {
            ipo = ip;
            System.out.println(ipo);
            client cl = new client();
            Thread t = new Thread(cl);
            t.start();
        }

        class client implements Runnable {

            Socket sock;

            public void run() {
                try {
                    sock = new Socket(ipo, 9008);
                    DataInputStream dis = new DataInputStream(sock.getInputStream());
                    DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
                    String msg = dis.readLine();
                    System.out.println(msg);
                    int i = 0;
                    while (true) {
                        File f = new File("F:\\xyz client\\kul" + i + ".jpg");
                        FileOutputStream fos = new FileOutputStream(f);

                        long lenght = dis.readLong();
                        int count = 0;
                        byte b[] = new byte[100];
                        while (true) {
                            int r = dis.read(b, 0, 100);
                            count += r;
                            fos.write(b, 0, r);
                            if (count == lenght) {
                                fos.close();
                                break;
                            }
                        }
                        fos.close();

                        dos.writeBytes("file recieved " + i + "\r\n");
                        BufferedImage bm = ImageIO.read(f);

                        lb.setIcon(new ImageIcon(bm));

                        i++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
