package ejercicioexpojuego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Nivel1 extends JFrame {
    private JPanel fondoPanel;
    private JButton generarBoton;
    private ArrayList<Cuadro> cuadrosFijos;
    private ArrayList<Cuadro> cuadrosMovibles;
    private Cuadro cuadroSeleccionado = null;
    private Timer timer;
    private Random random;

    public Nivel1() {
        setTitle("Nivel 1 - Generar y Mover Cuadros");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        fondoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Cuadro cuadro : cuadrosFijos) {
                    g.setColor(cuadro.color);
                    g.drawRect(cuadro.x, cuadro.y, cuadro.ancho, cuadro.alto);
                }
                for (Cuadro cuadro : cuadrosMovibles) {
                    g.setColor(cuadro.color);
                    g.fillRect(cuadro.x, cuadro.y, cuadro.ancho, cuadro.alto);
                }
            }
        };
        fondoPanel.setBounds(0, 0, 800, 500);
        fondoPanel.setBackground(Color.WHITE);
        fondoPanel.setLayout(null);
        add(fondoPanel);

        generarBoton = new JButton("Generar Cuadro");
        generarBoton.setBounds(300, 510, 200, 30);
        generarBoton.addActionListener(e -> generarCuadroMovible());
        add(generarBoton);

        cuadrosFijos = new ArrayList<>();
        cuadrosMovibles = new ArrayList<>();
        random = new Random();
        generarCuadrosFijos();

        fondoPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                seleccionarCuadro(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (cuadroSeleccionado != null) {
                    cuadroSeleccionado.setSeleccionado(false);
                    cuadroSeleccionado = null;
                    verificarVictoria();
                }
            }
        });

        fondoPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (cuadroSeleccionado != null) {
                    cuadroSeleccionado.x = e.getX() - cuadroSeleccionado.ancho / 2;
                    cuadroSeleccionado.y = e.getY() - cuadroSeleccionado.alto / 2;
                    fondoPanel.repaint();
                }
            }
        });

        // Iniciar el temporizador para cambiar la posición de los cuadros cada 15 segundos
        iniciarCambioDePosicion();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void generarCuadrosFijos() {
        // 6 cuadros más pequeños (80x60 en lugar de los grandes originales)
        cuadrosFijos.add(new Cuadro(50, 100, 80, 60, Color.RED));
        cuadrosFijos.add(new Cuadro(200, 50, 80, 60, Color.GREEN));
        cuadrosFijos.add(new Cuadro(400, 80, 80, 60, Color.BLUE));
        cuadrosFijos.add(new Cuadro(300, 200, 80, 60, Color.YELLOW));
        cuadrosFijos.add(new Cuadro(500, 250, 80, 60, Color.MAGENTA));
        cuadrosFijos.add(new Cuadro(600, 300, 80, 60, Color.ORANGE));
    }

    private void generarCuadroMovible() {
        if (cuadrosMovibles.size() >= cuadrosFijos.size()) {
            JOptionPane.showMessageDialog(this, "¡Todos los cuadros ya han sido generados!");
            return;
        }
        Cuadro cuadroBase = cuadrosFijos.get(cuadrosMovibles.size());
        Cuadro nuevoCuadro = new Cuadro(600, 400, cuadroBase.ancho, cuadroBase.alto, cuadroBase.color);
        cuadrosMovibles.add(nuevoCuadro);
        fondoPanel.repaint();
    }

    private void seleccionarCuadro(Point p) {
        for (Cuadro cuadro : cuadrosMovibles) {
            if (p.x >= cuadro.x && p.x <= cuadro.x + cuadro.ancho &&
                p.y >= cuadro.y && p.y <= cuadro.y + cuadro.alto) {
                cuadroSeleccionado = cuadro;
                cuadro.setSeleccionado(true);
                return;
            }
        }
    }

    private void verificarVictoria() {
        int correctos = 0;
        for (int i = 0; i < cuadrosMovibles.size(); i++) {
            Cuadro movible = cuadrosMovibles.get(i);
            Cuadro fijo = cuadrosFijos.get(i);

            if (Math.abs(movible.x - fijo.x) < 10 && Math.abs(movible.y - fijo.y) < 10) {
                correctos++;
            }
        }

        if (correctos == cuadrosFijos.size()) {
            JOptionPane.showMessageDialog(this, "¡Ganaste! Pasando al Nivel 2...");
            abrirNivel2();
        }
    }

    private void abrirNivel2() {
        dispose(); // Cierra Nivel1
        new Nivel2(); // Abre Nivel2
    }

    private void iniciarCambioDePosicion() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                cambiarPosiciones();
            }
        }, 0, 15000); // Cambia posiciones cada 15 segundos
    }

    private void cambiarPosiciones() {
        for (Cuadro cuadro : cuadrosFijos) {
            int nuevoX = random.nextInt(700);
            int nuevoY = random.nextInt(400);
            cuadro.x = nuevoX;
            cuadro.y = nuevoY;
        }
        fondoPanel.repaint();
    }

    private static class Cuadro {
        int x, y, ancho, alto;
        Color color;
        private boolean seleccionado;

        public Cuadro(int x, int y, int ancho, int alto, Color color) {
            this.x = x;
            this.y = y;
            this.ancho = ancho;
            this.alto = alto;
            this.color = color;
            this.seleccionado = false;
        }

        public void setSeleccionado(boolean seleccionado) {
            this.seleccionado = seleccionado;
        }
    }

    public static void main(String[] args) {
        new Nivel1();
    }
}