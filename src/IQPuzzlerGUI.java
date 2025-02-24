import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class IQPuzzlerGUI {
    private JFrame frame;
    private JPanel boardPanel;
    private JLabel executionLabel;
    private JLabel statusLabel;
    private JLabel timeLabel;
    private Solver solver;
    private JButton solveButton;
    private JButton saveButton;

    public IQPuzzlerGUI() {
        frame = new JFrame("IQ Puzzler Solver");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(400, 400));
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainPanel.add(boardPanel);

        statusLabel = new JLabel("Pilih file dan klik Solve");
        frame.add(statusLabel, BorderLayout.SOUTH);

        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        timeLabel = new JLabel("Waktu eksekusi: - ms");
        executionLabel = new JLabel("Banyak Kasus: -");
        infoPanel.add(timeLabel);
        infoPanel.add(executionLabel);
        frame.add(infoPanel, BorderLayout.NORTH);

        JButton loadButton = new JButton("Load File");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        solver = new Solver(selectedFile.getAbsolutePath());
                        statusLabel.setText("File Loaded: " + selectedFile.getName());
                        solveButton.setEnabled(true);
                        saveButton.setEnabled(false);
                        timeLabel.setText("Waktu eksekusi: - ms");
                        executionLabel.setText("Banyak Kasus: -");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Terjadi kesalahan saat memuat file!\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        solveButton = new JButton("Solve");
        solveButton.setEnabled(false);
        solveButton.addActionListener(e -> {
            if (solver != null) {
                try {
                    long startTime = System.currentTimeMillis();
                    solver.trysolve(solver.getIqPuzzler().getListOfPieces());
                    long endTime = System.currentTimeMillis();
                    if (solver.done) {
                        drawBoard();
                        statusLabel.setText("Puzzle Solved!");
                        saveButton.setEnabled(true);
                        timeLabel.setText("Waktu eksekusi: " + (endTime - startTime) + " ms");
                        executionLabel.setText("Banyak Kasus: " + solver.getCasesChecked());
                    } else {
                        boardPanel.removeAll();
                        boardPanel.revalidate(); 
                        boardPanel.repaint();    
                        statusLabel.setText("Tidak ada Solusi!");
                        timeLabel.setText("Waktu eksekusi: " + (endTime - startTime) + " ms");
                        executionLabel.setText("Banyak Kasus: " + solver.getCasesChecked());
                        
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Terjadi kesalahan saat memuat file!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(e -> {
            if (solver != null && solver.getIqPuzzler() != null) {
                solver.makeTxtFile();
                solver.saveToFile("solution.txt");
                statusLabel.setText("Solution saved to solution.txt");
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(loadButton);
        controlPanel.add(solveButton);
        controlPanel.add(saveButton);

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.add(mainPanel, BorderLayout.CENTER);
        centerContainer.add(controlPanel, BorderLayout.SOUTH);

        frame.add(centerContainer, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void drawBoard() {
        boardPanel.removeAll();
        int rows = solver.getIqPuzzler().getRow();
        int cols = solver.getIqPuzzler().getColumn();
        boardPanel.setLayout(new GridLayout(rows, cols));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                JLabel cell = new JLabel("", SwingConstants.CENTER);
                cell.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                cell.setOpaque(true);

                char piece = solver.getPieceAbjadInBoard(i, j);
                if (piece == '*') {
                    cell.setBackground(Color.WHITE);
                } else {
                    cell.setText(String.valueOf(piece));
                    Color puzzlePieceColor = solver.getIqPuzzler().getPieceColor().get(piece);
                    if (puzzlePieceColor != null) {
                        cell.setBackground(puzzlePieceColor);
                    } else {
                        cell.setBackground(Color.LIGHT_GRAY);
                    }
                }
                boardPanel.add(cell);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(IQPuzzlerGUI::new);
    }
}
