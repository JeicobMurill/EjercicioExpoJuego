/**
 *
 * @author jeico
 */

package ejercicioexpojuego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EjercicioExpoJuego extends JFrame {
    private JPanel fondoPanel;
    private JButton generarBoton;
    private ArrayList<Cuadro> cuadrosFijos;
    private ArrayList<Cuadro> cuadrosMovibles;
    private Cuadro cuadroSeleccionado = null; // Almacena el cuadro actualmente seleccionado para que no se muevan todos al tiempo

    public EjercicioExpoJuego() {
        setTitle("Generar y Mover Cuadros");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Panel de fondo
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

        // Botón para generar cuadros movibles
        generarBoton = new JButton("Generar Cuadro");
        generarBoton.setBounds(300, 510, 200, 30);
        generarBoton.addActionListener(e -> generarCuadroMovible());
        add(generarBoton);

        // Inicializar listas de cuadros
        cuadrosFijos = new ArrayList<>();
        cuadrosMovibles = new ArrayList<>();
        generarCuadrosFijos();

        // Listeners para el movimiento del mouse
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

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Método para crear los cuadros fijos en el fondo
    private void generarCuadrosFijos() {
        cuadrosFijos.add(new Cuadro(50, 100, 200, 150, Color.RED));
        cuadrosFijos.add(new Cuadro(120, 50, 250, 200, Color.GREEN));
        cuadrosFijos.add(new Cuadro(400, 80, 180, 120, Color.RED));
        cuadrosFijos.add(new Cuadro(300, 200, 220, 180, Color.GREEN));
        cuadrosFijos.add(new Cuadro(500, 250, 250, 160, Color.RED));
    }

    // Método para generar un cuadro movible
    private void generarCuadroMovible() {
        if (cuadrosMovibles.size() >= cuadrosFijos.size()) {
            JOptionPane.showMessageDialog(this, "¡Todos los cuadros ya han sido generados!");
            return;
        }

        // Obtener el cuadro fijo correspondiente
        Cuadro cuadroBase = cuadrosFijos.get(cuadrosMovibles.size());

        // Crear un nuevo cuadro movible con el mismo color y tamaño del cuadro fijo
        Cuadro nuevoCuadro = new Cuadro(600, 400, cuadroBase.ancho, cuadroBase.alto, cuadroBase.color);
        cuadrosMovibles.add(nuevoCuadro);
        fondoPanel.repaint();
    }

    // Método para seleccionar un cuadro cuando el usuario hace clic
    private void seleccionarCuadro(Point p) {
        for (Cuadro cuadro : cuadrosMovibles) {
            if (p.x >= cuadro.x && p.x <= cuadro.x + cuadro.ancho &&
                p.y >= cuadro.y && p.y <= cuadro.y + cuadro.alto) {
                cuadroSeleccionado = cuadro;
                cuadro.setSeleccionado(true);
                return; // Solo selecciona un cuadro a la vez
            }
        }
    }

    // Método para verificar si todos los cuadros están en su lugar correcto
    private void verificarVictoria() {
        int correctos = 0;
        for (int i = 0; i < cuadrosMovibles.size(); i++) {
            Cuadro movible = cuadrosMovibles.get(i);
            Cuadro fijo = cuadrosFijos.get(i);

            // Comprobar si el cuadro movible está cerca de su posición correcta
            if (Math.abs(movible.x - fijo.x) < 10 && Math.abs(movible.y - fijo.y) < 10) {
                correctos++;
            }
        }

        // Si todos los cuadros están en su lugar, mostrar mensaje de victoria
        if (correctos == cuadrosFijos.size()) {
            JOptionPane.showMessageDialog(this, "¡Ganaste! Todos los cuadros están en su posición correcta.");
        }
    }

    // Clase para definir los cuadros (fijos y movibles)
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

        public boolean isSeleccionado() {
            return seleccionado;
        }

        public void setSeleccionado(boolean seleccionado) {
            this.seleccionado = seleccionado;
        }
    }

    public static void main(String[] args) {
        new EjercicioExpoJuego();
    }
}