package ejercicioexpojuego;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Nivel2 extends JFrame {

    private JPanel fondoPanel;
    private JButton botonAmarillo, botonAzul, botonRojo, botonReiniciar;
    private ArrayList<Cuadro> cuadrosFijos;
    private ArrayList<Cuadro> cuadrosMovibles;
    private Cuadro cuadroSeleccionado = null;
    private Map<Cuadro, Set<Color>> combinacionesFijas;

    public Nivel2() {
        setTitle("Nivel 2: Organizar Cuadros");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Panel de fondo
        fondoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Cuadro cuadro : cuadrosFijos) {
                    if (cuadro.completado) {
                        g.setColor(Color.GRAY); // Color gris para cuadros completados
                        g.fillRect(cuadro.x, cuadro.y, cuadro.ancho, cuadro.alto);
                    } else {
                        g.setColor(Color.WHITE); // Relleno blanco para los cuadros fijos
                        g.fillRect(cuadro.x, cuadro.y, cuadro.ancho, cuadro.alto);
                    }
                    // Dibuja el borde con el color correspondiente al fondo
                    g.setColor(cuadro.color);
                    g.drawRect(cuadro.x, cuadro.y, cuadro.ancho, cuadro.alto);
                }

                for (Cuadro cuadro : cuadrosMovibles) {
                    if (cuadro.color == Color.GRAY) {
                        g.setColor(Color.GRAY); // Cuadro gris ya colocado
                    } else {
                        g.setColor(cuadro.color);
                    }
                    g.fillRect(cuadro.x, cuadro.y, cuadro.ancho, cuadro.alto);
                    g.setColor(Color.BLACK);
                    g.drawRect(cuadro.x, cuadro.y, cuadro.ancho, cuadro.alto);
                }
            }
        };
        fondoPanel.setBounds(0, 0, 800, 500);
        fondoPanel.setBackground(Color.WHITE);
        fondoPanel.setLayout(null);
        add(fondoPanel);

        // Botones para generar los cuadros
        botonAmarillo = new JButton("Generar Amarillo");
        botonAmarillo.setBounds(50, 510, 150, 30);
        botonAmarillo.addActionListener(e -> generarCuadro(Color.YELLOW));
        add(botonAmarillo);

        botonAzul = new JButton("Generar Azul");
        botonAzul.setBounds(210, 510, 150, 30);
        botonAzul.addActionListener(e -> generarCuadro(Color.BLUE));
        add(botonAzul);

        botonRojo = new JButton("Generar Rojo");
        botonRojo.setBounds(370, 510, 150, 30);
        botonRojo.addActionListener(e -> generarCuadro(Color.RED));
        add(botonRojo);

        botonReiniciar = new JButton("Reiniciar");
        botonReiniciar.setBounds(530, 510, 150, 30);
        botonReiniciar.addActionListener(e -> reiniciarJuego());
        add(botonReiniciar);

        // Inicializar las listas de cuadros
        cuadrosFijos = new ArrayList<>();
        cuadrosMovibles = new ArrayList<>();
        generarCuadrosFijos();
        inicializarCombinaciones();

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
                if (cuadroSeleccionado != null && cuadroSeleccionado.color != Color.GRAY) {
                    cuadroSeleccionado.x = e.getX() - cuadroSeleccionado.ancho / 2;
                    cuadroSeleccionado.y = e.getY() - cuadroSeleccionado.alto / 2;
                    fondoPanel.repaint();
                }
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Método para generar los cuadros fijos en el fondo
    private void generarCuadrosFijos() {
        cuadrosFijos.add(new Cuadro(50, 50, 100, 100, Color.YELLOW));  // Cuadro amarillo
        cuadrosFijos.add(new Cuadro(200, 50, 100, 100, Color.BLUE));    // Cuadro azul
        cuadrosFijos.add(new Cuadro(350, 50, 100, 100, Color.RED));     // Cuadro rojo
        cuadrosFijos.add(new Cuadro(50, 200, 100, 100, Color.GREEN));   // Cuadro verde
        cuadrosFijos.add(new Cuadro(200, 200, 100, 100, Color.MAGENTA));// Cuadro morado
        cuadrosFijos.add(new Cuadro(350, 200, 100, 100, Color.ORANGE)); // Cuadro naranja
        cuadrosFijos.add(new Cuadro(50, 350, 100, 100, new Color(139, 69, 19))); // Cuadro café
    }

    // Método para inicializar las combinaciones de colores para los cuadros fijos
    private void inicializarCombinaciones() {
        combinacionesFijas = new HashMap<>();
        combinacionesFijas.put(cuadrosFijos.get(3), new HashSet<>(Arrays.asList(Color.YELLOW, Color.BLUE)));  // Cuadro verde -> Amarillo + Azul
        combinacionesFijas.put(cuadrosFijos.get(5), new HashSet<>(Arrays.asList(Color.YELLOW, Color.RED)));   // Cuadro naranja -> Amarillo + Rojo
        combinacionesFijas.put(cuadrosFijos.get(4), new HashSet<>(Arrays.asList(Color.BLUE, Color.RED)));     // Cuadro morado -> Azul + Rojo
        combinacionesFijas.put(cuadrosFijos.get(6), new HashSet<>(Arrays.asList(Color.YELLOW, Color.BLUE, Color.RED))); // Cuadro café -> Amarillo + Azul + Rojo
    }

    // Método para generar un cuadro movible
    private void generarCuadro(Color color) {
        Cuadro nuevoCuadro = new Cuadro(600, 400, 100, 100, color);
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

    // Compara cada cuadro fijo con los cuadros movibles
    for (Cuadro fijo : cuadrosFijos) {
        Set<Color> coloresRequeridos = combinacionesFijas.get(fijo);

        // Solo verificamos si la lista de colores requeridos no está vacía
        if (coloresRequeridos != null) {
            Set<Color> coloresEnFijo = new HashSet<>();

            // Recorremos los cuadros movibles
            for (Cuadro movible : cuadrosMovibles) {
                if (Math.abs(movible.x - fijo.x) < 20 && Math.abs(movible.y - fijo.y) < 20) {
                    coloresEnFijo.add(movible.color);
                }
            }

            // Si los colores movibles coinciden con los colores requeridos, marca como completado
            if (coloresEnFijo.containsAll(coloresRequeridos)) {
                fijo.completado = true;
                correctos++;
            }
        }
    }

    // Si todos los cuadros están completos, muestra el mensaje de victoria
    if (correctos == combinacionesFijas.size()) {
        JOptionPane.showMessageDialog(this, "¡Ganaste! Todos los cuadros están en su posición correcta.");
        // Cierra el programa automáticamente después de la victoria
        System.exit(0);
    }
}

    // Método para reiniciar el juego
    private void reiniciarJuego() {
        cuadrosMovibles.clear();
        for (Cuadro cuadro : cuadrosFijos) {
            cuadro.completado = false; // Reinicia el estado de los cuadros fijos
        }
        fondoPanel.repaint();
    }

    // Clase para definir los cuadros (fijos y movibles)
    private static class Cuadro {
        int x, y, ancho, alto;
        Color color;
        boolean seleccionado;
        boolean completado;

        public Cuadro(int x, int y, int ancho, int alto, Color color) {
            this.x = x;
            this.y = y;
            this.ancho = ancho;
            this.alto = alto;
            this.color = color;
            this.seleccionado = false;
            this.completado = false;
        }

        public boolean isSeleccionado() {
            return seleccionado;
        }

        public void setSeleccionado(boolean seleccionado) {
            this.seleccionado = seleccionado;
        }
    }

    public static void main(String[] args) {
        new Nivel2();
    }
}