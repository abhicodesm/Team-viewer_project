
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class serversock7thm {

    clienthandler cl;
    Socket sock;
    Robot rbt;

    public static void main(String[] args) throws IOException {

        serversock7thm ser = new serversock7thm();

    }

    public serversock7thm() {
        try {
            ServerSocket sersock = new ServerSocket(9000);
            new file_server();
            rbt = new Robot();

            while (true) {

                sock = sersock.accept();
                cl = new clienthandler(sock);
                Thread t = new Thread(cl);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class clienthandler implements Runnable {

        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        Socket s;

        public clienthandler(Socket sock) {
            s = sock;
        }

        @Override
        public void run() {
            try {
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                String ser = "hello client";
                while (true) {
                    String s = dis.readLine();
                    if (ser.equals(s)) {
                        System.out.println("hello client");
                        dos.writeBytes("hello server\r\n");
                        break;
                    } else if (s.equals("Send Screen")) {
                        System.out.println(s);
                        dos.writeBytes("request recieved\r\n");
                        dos.writeBytes(width + "\r\n");
                        dos.writeBytes(height + "\r\n");
                    } else if (s.equals("MouseMoved")) {
                        int xm = dis.read();
                        int ym = dis.read();
                        rbt.mouseMove(xm, ym);

                    } else if (s.equals("MouseDraggged")) {
                        int xd = dis.read();
                        int yd = dis.read();
                        rbt.mouseMove(xd, yd);

                    } else if (s.equals("MouseClicked")) {
                        int x = dis.readInt();
                        int y = dis.readInt();
                        int bt = dis.readInt();
                        rbt.mouseMove(x, y);
                        if (bt == 1) {
                            rbt.mousePress(MouseEvent.BUTTON1_MASK);
                        } else if (bt == 2) {
                            rbt.mousePress(MouseEvent.BUTTON2_MASK);
                        } else if (bt == 3) {
                            rbt.mousePress(MouseEvent.BUTTON3_MASK);
                        }
                        
                    } else if (s.equals("MousePressed")) {
                        int x = dis.read();
                        int y = dis.read();
                        int bt = dis.read();
                          rbt.mouseMove(x, y);
                        if (bt == 1) {
                            rbt.mousePress(MouseEvent.BUTTON1_MASK);
                        } else if (bt == 2) {
                            rbt.mousePress(MouseEvent.BUTTON2_MASK);
                        } else if (bt == 3) {
                            rbt.mousePress(MouseEvent.BUTTON3_MASK);
                        }

                      

                    } else if (s.equals("MouseReleased")) {
                        int x = dis.read();
                        int y = dis.read();
                        int bt = dis.read();
                         rbt.mouseMove(x, y);
                        if (bt == 1) {
                            rbt.mouseRelease(MouseEvent.BUTTON1_MASK);
                        } else if (bt == 2) {
                            rbt.mouseRelease(MouseEvent.BUTTON2_MASK);
                        } else if (bt == 3) {
                            rbt.mouseRelease(MouseEvent.BUTTON3_MASK);
                        }
                       

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class file_server {

        Robot r;

        public file_server() throws AWTException {
            r = new Robot();
            serverr ser = new serverr();
            Thread th = new Thread(ser);
            th.start();
        }

        class serverr implements Runnable {

            @Override
            public void run() {
                try {
                    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
                    int height = Toolkit.getDefaultToolkit().getScreenSize().height;
                    ServerSocket sersock = new ServerSocket(9008);
                    Socket sock;
                    sock = sersock.accept();
                    DataInputStream dis = new DataInputStream(sock.getInputStream());
                    DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
                    String s = "Hello";
                    dos.writeBytes(s + "\r\n");
                    while (true) {
                        BufferedImage screen = r.createScreenCapture(new Rectangle(width, height));
                        ImageIO.write(screen, "jpg", new File("F:\\imagesbyserver\\xyz.jpg"));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            //Logger.getLogger(screenshot.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        File f = new File("F:\\imagesbyserver\\xyz.jpg");
                        dos.writeLong(f.length());
                        FileInputStream fis = new FileInputStream("F:\\imagesbyserver\\xyz.jpg");
                        byte b[] = new byte[100];
                        while (true) {
                            int rr = fis.read(b, 0, 100);
                            if (rr == -1) {
                                break;
                            }
                            dos.write(b, 0, rr);
                        }
                        String msg = dis.readLine();
                        System.out.println(msg);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

}
