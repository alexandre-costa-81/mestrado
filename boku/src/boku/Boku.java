package boku;

import bitboard.BokuBoard;
import bitboard.LookupTable;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import tpzsgames.BoardType;
import tpzsgames.EngineType;
import tpzsgames.GameType;
import tpzsgames.MoveType;
import tpzsgames.PlayerType;
import treesearch.AlphaBeta;

public class Boku extends JApplet implements GameType {
    private static final int AUTO_PLAY_GAMES = 100;
    private static final int BLACK_MAXPLY = 3;
    private static final int WHITE_MAXPLY = 3;
    private static final boolean BLACK_PLAYS_EXPERIMENTAL = false;
    private static final boolean WHITE_PLAYS_EXPERIMENTAL = true;
    private static final boolean BLACK_RANDOM = true;
    private static final boolean WHITE_RANDOM = true;
    private static BokuBoard board;
    private Controller controller;
    private Statistics statistics;
    private static int h;
    private static int d;
    public static final int WINNER = 5;
    public Player playerBlack;
    public Player playerWhite;
    private Move[] history;
    private int nextMovePointer;
    protected int maxMoves;
    private int maxPosMoves;
    private int playersTurn;
    private boolean gameRunning;
    private boolean autoplay;
    public static volatile boolean threadRunning;
    private JMenuItem stopGameItem;
    private Field noEntryField;
    private static Box windowPanel;
    private static Box boardPanel;
    private static Box[] rowPanel;
    private static JButton[] fieldButton;
    private static Box sidePanel;
    Options options;
    private static JTextArea historyNotePad;
    private static String historyStr;
    private static JScrollPane historyScrollPane;
    private static JTextArea internalBoardPane;
    private static JTextArea moveListBoardPane;
    private static final Color TABLE_COLOUR = new Color(98049);
    private static final int BASE_WIDTH = 48;
    private static final int BASE_HEIGHT = 44;
    private static final Dimension BASE_FIELD = new Dimension(48, 44);
    private static final int MARGIN = 48;
    private static int defaultWidth;
    private static int defaultHeight;
    private static Dimension defaultDimension;
    private static final int SIDE_PANEL_WIDTH = 220;
    private static boolean isApplet = true;
    private static boolean captureMove;
    private static Field moveField;
    private static Field[] captureFields;
    private static Field[] winnerFields;
    private static Icon borderLTIcon;
    private static Icon borderRTIcon;
    private static Icon borderLCIcon;
    private static Icon borderRCIcon;
    private static Icon borderLBIcon;
    private static Icon borderRBIcon;
    private static Icon borderTLIcon;
    private static Icon borderTRIcon;
    private static Icon borderTTIcon;
    private static Icon borderBBIcon;
    private static Icon borderBLIcon;
    private static Icon borderBRIcon;
    private static Icon borderTSingleIcon;
    private static Icon borderBSingleIcon;
    private static Icon fieldIcon;
    private static Icon fieldRolloverIcon;
    private static Icon fieldPressedIcon;
    private static Icon blackMarbleIcon;
    private static Icon blackMarbleRolloverIcon;
    private static Icon blackMarblePressedIcon;
    private static Icon blackMarbleCaptureIcon;
    private static Icon whiteMarbleIcon;
    private static Icon whiteMarbleRolloverIcon;
    private static Icon whiteMarblePressedIcon;
    private static Icon whiteMarbleCaptureIcon;
    public static Icon arrowLIcon;
    public static Icon arrowRIcon;
    public static Icon arrowLDisabledIcon;
    public static Icon arrowRDisabledIcon;
    private static final String CR = System.getProperty("line.separator");
    private static final String authorString = "Christoph Schönberger";
    private static final String versionString = "1.21 Development release (09.01.2005)";
    private static final String yearString = "2002-2005";
    private static final String aboutString = "Boku version 1.21 Development release (09.01.2005)"
            + CR
            + "by "
            + "Christoph Schönberger"
            + CR
            + "c "
            + "2002-2005";
    private static final String imageFolder = "../images/";
    private static final String borderTT = "_borderTT.jpg";
    private static final String borderBB = "_borderBB.jpg";
    private static final String borderTR = "_borderTR.jpg";
    private static final String borderBR = "_borderBR.jpg";
    private static final String borderTL = "_borderTL.jpg";
    private static final String borderBL = "_borderBL.jpg";
    private static final String borderLT = "_borderLT.jpg";
    private static final String borderRT = "_borderRT.jpg";
    private static final String borderLC = "_borderLC.jpg";
    private static final String borderRC = "_borderRC.jpg";
    private static final String borderLB = "_borderLB.jpg";
    private static final String borderRB = "_borderRB.jpg";
    private static final String borderT = "_borderTSingle.jpg";
    private static final String borderB = "_borderBSingle.jpg";
    private static final String fieldImage = "fieldImage.jpg";
    private static final String fieldRolloverImage = "fieldRolloverImage.jpg";
    private static final String fieldPressedImage = "fieldPressedImage.jpg";
    private static final String blackMarble = "blackMarble.jpg";
    private static final String blackMarbleRollover = "blackMarbleRollover.jpg";
    private static final String blackMarblePressed = "blackMarblePressed.jpg";
    private static final String blackMarbleCapture = "blackMarbleCapture.jpg";
    private static final String whiteMarble = "whiteMarble.jpg";
    private static final String whiteMarbleRollover = "whiteMarbleRollover.jpg";
    private static final String whiteMarblePressed = "whiteMarblePressed.jpg";
    private static final String whiteMarbleCapture = "whiteMarbleCapture.jpg";
    private static final String arrowL = "arrowL.jpg";
    private static final String arrowLDisabled = "arrowLDisabled.jpg";
    private static final String arrowR = "arrowR.jpg";
    private static final String arrowRDisabled = "arrowRDisabled.jpg";

    public void init() {
        JRootPane rootPane = getRootPane();
        rootPane.putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);

        if (isApplet) {
            String hString = getParameter("h");
            String dString = getParameter("d");
            try {
                parseParameters(hString, dString);
            } catch (RuntimeException re) {
                showStatus(re.getMessage());
                throw new RuntimeException();
            }
        }
        board = new BokuBoard(h, d);
        this.autoplay = false;

        defaultWidth = board.getWidth() * 48 + 48;
        defaultHeight = board.getHeight() * 44 + 192;
        defaultDimension = new Dimension(defaultWidth + 220 + 144, defaultHeight);
        setSize(defaultDimension);

        this.maxMoves = (board.map.numberOfFields + 60);
        this.maxPosMoves = (board.map.numberOfFields + 10);

        this.playerBlack = new Player(this, 1, true);

        this.playerBlack.getEngine().setMaxPly(3);

        this.playerWhite = new Player(this, -1, false);
        this.playerWhite.getEngine().setMaxPly(3);

        this.playerWhite.setLookupTable(LookupTable.EXPERIMENTAL_TABLE);
        this.playerBlack.getEngine().setRandomChoice(true);
        this.playerWhite.getEngine().setRandomChoice(true);

        this.history = new Move[this.maxMoves];
        captureFields = new Field[12];
        winnerFields = new Field[5];

        this.options = new Options(this);
        initialiseImages();
        initialiseBoardComponents();
        setUpMenus();
        this.statistics = new Statistics();
        if (isApplet) {
            LogWriter.switchTo(0);
        }
        newGame();
    }

    public void start() {
    }

    ImageIcon getImageIcon(String fileName) throws IOException {
        InputStream in = getClass().getResourceAsStream(fileName);
        byte[] buffer = new byte[in.available()];
        in.read(buffer);
        Image image = Toolkit.getDefaultToolkit().createImage(buffer);
        return new ImageIcon(image);
    }

    private void initialiseImages() {
        try {
            borderTTIcon = getImageIcon("../images/_borderTT.jpg");
            borderBBIcon = getImageIcon("../images/_borderBB.jpg");
            borderTLIcon = getImageIcon("../images/_borderTL.jpg");
            borderBLIcon = getImageIcon("../images/_borderBL.jpg");
            borderTRIcon = getImageIcon("../images/_borderTR.jpg");
            borderBRIcon = getImageIcon("../images/_borderBR.jpg");
            borderLTIcon = getImageIcon("../images/_borderLT.jpg");
            borderRTIcon = getImageIcon("../images/_borderRT.jpg");
            borderLCIcon = getImageIcon("../images/_borderLC.jpg");
            borderRCIcon = getImageIcon("../images/_borderRC.jpg");
            borderLBIcon = getImageIcon("../images/_borderLB.jpg");
            borderRBIcon = getImageIcon("../images/_borderRB.jpg");
            borderTSingleIcon = getImageIcon("../images/_borderTSingle.jpg");
            borderBSingleIcon = getImageIcon("../images/_borderBSingle.jpg");

            fieldIcon = getImageIcon("../images/fieldImage.jpg");
            fieldRolloverIcon = getImageIcon("../images/fieldRolloverImage.jpg");
            fieldPressedIcon = getImageIcon("../images/fieldPressedImage.jpg");

            blackMarbleIcon = getImageIcon("../images/blackMarble.jpg");
            blackMarbleRolloverIcon = getImageIcon("../images/blackMarbleRollover.jpg");
            blackMarblePressedIcon = getImageIcon("../images/blackMarblePressed.jpg");
            blackMarbleCaptureIcon = getImageIcon("../images/blackMarbleCapture.jpg");
            whiteMarbleIcon = getImageIcon("../images/whiteMarble.jpg");
            whiteMarbleRolloverIcon = getImageIcon("../images/whiteMarbleRollover.jpg");
            whiteMarblePressedIcon = getImageIcon("../images/whiteMarblePressed.jpg");
            whiteMarbleCaptureIcon = getImageIcon("../images/whiteMarbleCapture.jpg");
            arrowLIcon = getImageIcon("../images/arrowL.jpg");
            arrowLDisabledIcon = getImageIcon("../images/arrowLDisabled.jpg");
            arrowRIcon = getImageIcon("../images/arrowR.jpg");
            arrowRDisabledIcon = getImageIcon("../images/arrowRDisabled.jpg");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Could not load images\n"
                    + getClass().toString() + "\n"
                    + e.getMessage(),
                    "Error",
                    0);
            System.exit(-1);
        }
    }

    private void initialiseBoardComponents() {
        Container container = getContentPane();

        windowPanel = new Box(0);
        sidePanel = new Box(1);
        sidePanel.setSize(new Dimension(220, defaultHeight));
        historyNotePad = new JTextArea();
        historyNotePad.setEditable(false);
        historyNotePad.setFont(new Font("Monospaced", 0, 16));
        historyNotePad.setTabSize(7);
        historyNotePad.setMargin(new Insets(15, 15, 15, 15));

        boardPanel = new Box(1);
        rowPanel = new Box[board.getHeight() + 2];

        windowPanel.add(boardPanel);
        windowPanel.add(sidePanel);
        container.add(windowPanel);

        boardPanel.add(Box.createVerticalGlue());
        for (int i = 0; i < board.getHeight() + 2; i++) {
            rowPanel[i] = new Box(0);
            boardPanel.add(rowPanel[i]);

            if ((i == 0) || (i > board.getHeight())) {
                rowPanel[i].setPreferredSize(new Dimension(defaultWidth, borderTTIcon.getIconHeight()));
            } else {
                rowPanel[i].setPreferredSize(new Dimension(defaultWidth, 44));
            }
            rowPanel[i].setBackground(TABLE_COLOUR);
        }
        boardPanel.add(Box.createVerticalGlue());

        if (board.map.h == 1) {
            rowPanel[0].add(new JLabel(borderTSingleIcon));
        } else {
            rowPanel[0].add(new JLabel(borderTLIcon));
            for (int i = 0; i < board.map.h - 2; i++) {
                rowPanel[0].add(new JLabel(borderTTIcon));
            }
            rowPanel[0].add(new JLabel(borderTRIcon));
        }

        if (board.map.h == 1) {
            rowPanel[(board.getHeight() + 1)].add(new JLabel(borderBSingleIcon));
        } else {
            rowPanel[(board.getHeight() + 1)].add(new JLabel(borderBLIcon));
            for (int i = 0; i < board.map.h - 2; i++) {
                rowPanel[(board.getHeight() + 1)].add(new JLabel(borderBBIcon));
            }
            rowPanel[(board.getHeight() + 1)].add(new JLabel(borderBRIcon));
        }

        fieldButton = new JButton[board.map.numberOfFields];
        this.controller = new Controller();
        int panelNumber = 1;
        for (int i = 0; i < board.map.numberOfFields; i++) {
            fieldButton[i] = new JButton(fieldIcon);
            fieldButton[i].setRolloverIcon(fieldRolloverIcon);
            fieldButton[i].setPressedIcon(fieldPressedIcon);
            fieldButton[i].setDisabledIcon(fieldIcon);
            fieldButton[i].setBorderPainted(false);
            fieldButton[i].setPreferredSize(BASE_FIELD);
            if (board.map.firstInLine(i)) {
                rowPanel[panelNumber].add(Box.createHorizontalGlue());
                if (board.map.isTopHalf(i)) {
                    rowPanel[panelNumber].add(new JLabel(borderLTIcon));
                } else if (board.map.isBottomHalf(i)) {
                    rowPanel[panelNumber].add(new JLabel(borderLBIcon));
                } else {
                    rowPanel[panelNumber].add(new JLabel(borderLCIcon));
                }
            }
            rowPanel[panelNumber].add(fieldButton[i]);
            fieldButton[i].setActionCommand(String.valueOf(i));
            fieldButton[i].addActionListener(this.controller);
            if (board.map.lastInLine(i)) {
                if (board.map.isTopHalf(i)) {
                    rowPanel[panelNumber].add(new JLabel(borderRTIcon));
                } else if (board.map.isBottomHalf(i)) {
                    rowPanel[panelNumber].add(new JLabel(borderRBIcon));
                } else {
                    rowPanel[panelNumber].add(new JLabel(borderRCIcon));
                }
                rowPanel[panelNumber].add(Box.createHorizontalGlue());
                panelNumber++;
            }
        }

        historyScrollPane = new JScrollPane(historyNotePad);
        container.setBackground(TABLE_COLOUR);

        showHistoryPanel(this.options.showHistoryPanel());
        internalBoardPane = new JTextArea();
        internalBoardPane.setFont(new Font("Monospaced", 0, 16));
        internalBoardPane.setEditable(false);
        moveListBoardPane = new JTextArea();
        moveListBoardPane.setFont(new Font("Monospaced", 0, 14));
        moveListBoardPane.setEditable(false);
    }

    private void setUpMenus() {
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic('G');

        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.setMnemonic('N');
        newGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Boku.this.resetButtons();
                Boku.this.newGame();
            }
        });
        gameMenu.add(newGameItem);

        JMenuItem autoplayItem = new JMenuItem("Auto Play");
        autoplayItem.setMnemonic('A');
        autoplayItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Boku.this.autoplay = ((Boku.this.playerBlack.isComputer()) && (Boku.this.playerWhite.isComputer()));
                if (Boku.this.autoplay) {
                    Boku.this.resetButtons();
                    Boku.this.statistics.reset();
                    Boku.this.newGame();
                } else {
                    JOptionPane.showMessageDialog(
                            null,
                            "Can't use autoplay because at least one of\nthe players is not set to computer mode.\nPlease select engine in player setup menu first.",
                            "Error",
                            0);
                }

            }
        });
        gameMenu.add(autoplayItem);

        this.stopGameItem = new JMenuItem("Stop Game");
        this.stopGameItem.setMnemonic('S');
        this.stopGameItem.setEnabled((this.gameRunning) && (threadRunning) && (!this.autoplay));
        this.stopGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Boku.threadRunning = false;

                Boku.this.stopGame();
                Boku.this.stopGameItem.setEnabled(false);
                JOptionPane.showMessageDialog(null, "Game interrupted by user",
                        "Game stopped",
                        -1);
            }
        });
        gameMenu.add(this.stopGameItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic('X');

        if (isApplet) {
            exitItem.setEnabled(false);
        }
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(
                        null,
                        "Do you want to quit?",
                        "Exit Boku",
                        0) == 0) {

                    System.exit(0);
                }
            }
        });
        gameMenu.add(exitItem);

        JMenu optionsMenu = this.options.createOptionsMenu();

        JMenu playersMenu = new JMenu("Players & Engines");
        playersMenu.setMnemonic('P');
        playersMenu.setEnabled(true);

        JMenuItem blackPlayerItem = new JMenuItem("Black        ");
        blackPlayerItem.setMnemonic('B');
        blackPlayerItem.setEnabled(true);
        playersMenu.add(blackPlayerItem);
        blackPlayerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PlayerOptionPane.getInstance(Boku.this.playerBlack).showOptionPane();
            }
        });
        JMenuItem whitePlayerItem = new JMenuItem("White");
        whitePlayerItem.setMnemonic('W');
        whitePlayerItem.setEnabled(true);
        playersMenu.add(whitePlayerItem);
        whitePlayerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PlayerOptionPane.getInstance(Boku.this.playerWhite).showOptionPane();
            }
        });
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setMnemonic('A');
        aboutItem.setEnabled(true);
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, Boku.aboutString + Boku.CR + Boku.CR
                        + "Java version "
                        + System.getProperty("java.version")
                        + " by "
                        + System.getProperty("java.vendor")
                        + Boku.CR
                        + "running on "
                        + System.getProperty("os.name")
                        + ", v. "
                        + System.getProperty("os.version")
                        + Boku.CR
                        + Boku.CR
                        + "Board:"
                        + Boku.CR
                        + "Base row horizontal: "
                        + Boku.board.map.h
                        + Boku.CR
                        + "Base row diagonal: "
                        + Boku.board.map.d
                        + Boku.CR
                        + "Number of fields: "
                        + Boku.board.map.numberOfFields
                        + Boku.CR
                        + Boku.CR
                        + "Players:"
                        + Boku.CR
                        + "Black is a " + (Boku.this.playerBlack.isHuman()
                                ? "human"
                                : new StringBuffer("computer: ")
                                .append(Boku.CR)
                                .append(Boku.this.playerBlack.getEngine().toString()).append(", ")
                                .append(Boku.this.playerBlack.getLookupTable().toString()).toString())
                        + Boku.CR
                        + "White is a " + (Boku.this.playerWhite.isHuman()
                                ? "human"
                                : new StringBuffer("computer: ")
                                .append(Boku.CR)
                                .append(Boku.this.playerWhite.getEngine().toString()).append(", ")
                                .append(Boku.this.playerWhite.getLookupTable().toString()).toString())
                        + Boku.CR,
                        "About Boku",
                        -1);
            }
        });
        helpMenu.add(aboutItem);

        JMenuItem moveListItem = new JMenuItem("Show Static Evaluation");
        moveListItem.setMnemonic('M');
        moveListItem.setEnabled(true);
        moveListItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame popupWindow = new JFrame("Possible moves with static evaluation");
                popupWindow.setSize(700, 450);
                popupWindow.getContentPane().add(Boku.moveListBoardPane);
                popupWindow.show();
            }
        });
        helpMenu.add(moveListItem);

        JMenuItem printBoard = new JMenuItem("Internal Board");
        printBoard.setMnemonic('B');
        printBoard.setEnabled(true);
        printBoard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame popupWindow = new JFrame("Internal Board");
                popupWindow.setSize(400, 330);
                popupWindow.getContentPane().add(Boku.internalBoardPane);
                popupWindow.show();
            }
        });
        helpMenu.add(printBoard);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(gameMenu);
        menuBar.add(playersMenu);
        menuBar.add(optionsMenu);
        menuBar.add(helpMenu);
    }

    public void enableFlyOver(boolean choice) {
        for (int i = 0; i < board.map.numberOfFields; i++) {
            fieldButton[i].setToolTipText(choice ? "   Field " + (i + 1) : "");
        }
    }

    public void showHistoryPanel(boolean choice) {
        if (choice) {
            Dimension historyPanelSize = new Dimension(220, defaultHeight);
            historyScrollPane.setPreferredSize(historyPanelSize);
            sidePanel.add(historyScrollPane);
            historyScrollPane.repaint();
        } else {
            sidePanel.remove(historyScrollPane);
            sidePanel.repaint();
        }
    }

    public void make(MoveType move) {
        if (this.nextMovePointer == this.maxMoves) {
            throw new IndexOutOfBoundsException("Error: Can't make move. Stack overflow.");
        }
        Move thatMove = (Move) move;

        this.history[(this.nextMovePointer++)] = thatMove;

        this.noEntryField = null;

        board.set(thatMove.getMarble(), thatMove.getField());

        if (thatMove.getCaptureField() != null) {
            board.clear(thatMove.getCaptureField());
            this.noEntryField = thatMove.getCaptureField();
        }
        this.playersTurn = (-this.playersTurn);
    }

    public void undoMove() {
        if (this.nextMovePointer == 0) {
            throw new IndexOutOfBoundsException("Error: Can't undo move. Stack underflow.");
        }

        Move thatMove = this.history[(--this.nextMovePointer)];

        board.clear(thatMove.getField());

        if (thatMove.getCaptureField() != null) {
            Marble putBackMarble
                    = new Marble(-thatMove.getMarble().getColour());
            board.set(putBackMarble, thatMove.getCaptureField());

            this.noEntryField = null;
        }

        if (this.nextMovePointer > 0) {
            Move moveBefore = this.history[(this.nextMovePointer - 1)];

            if (moveBefore.getCaptureField() != null) {
                this.noEntryField = moveBefore.getCaptureField();
            }
        }
        this.playersTurn = (-this.playersTurn);
    }

    public void newGame() {
        if (!this.gameRunning) {
            board.clear();
            this.nextMovePointer = 0;
            this.playersTurn = 1;
            moveField = null;
            captureMove = false;
            this.noEntryField = null;
            historyStr
                    = " Game " + (this.statistics.getGameCounter() + 1)
                    + "\n"
                    + " Move\tBlack\tWhite\n";
            if (LogWriter.isPrintModus(1)) {
                LogWriter.out.print("\n" + historyStr);
            }
            historyNotePad.setText(historyStr);
            for (int i = 0; i < captureFields.length; i++) {
                captureFields[i] = null;
            }
            for (int i = 0; i < winnerFields.length; i++) {
                winnerFields[i] = null;
            }
            this.gameRunning = true;

            internalBoardPane.setText(board.toString());
            moveListBoardPane.setText(moveListToString());
            this.statistics.newGame();
        }

        if (nextPlayer().isComputer()) {
            new EngineThread().start();
        }
    }

    public int eval() {
        return board.evalBoard();
    }

    public int getLastEval() {
        return board.getLastEval();
    }

    public boolean won(int colour) {
        return board.won(colour);
    }

    public boolean won(PlayerType player) {
        return board.won(player.getColour());
    }

    public int getNextTurn() {
        return this.playersTurn;
    }

    public Player nextPlayer() {
        return this.playersTurn == 1 ? this.playerBlack : this.playerWhite;
    }

    public final boolean isAvailable(Field testField) {
        return (board.get(testField) == 0) && (!testField.isEqual(this.noEntryField));
    }

    public MoveType[] generateMoveList(int colour) {
        Move[] moveList = new Move[this.maxPosMoves];
        int endOfList = 0;
        for (int y = 0; y < board.map.maxFieldsInLine; y++) {
            for (int x = board.map.minX(y); x < board.map.maxX(y); x++) {
                Field nextField = new Field(x, y);
                if (isAvailable(nextField)) {
                    board.set(nextField, colour);
                    Field[] captureFields = board.getCaptureFields(nextField, colour);
                    board.clear(nextField);

                    if (captureFields[0] != null) {
                        int counter = 0;
                        do {
                            Move nextMove = new Move(nextField, captureFields[counter], new Marble(colour));
                            moveList[(endOfList++)] = nextMove;
                            counter++;
                        } while (captureFields[counter] != null);
                    } else {
                        Move nextMove = new Move(nextField, new Marble(colour));
                        moveList[(endOfList++)] = nextMove;
                    }
                }
            }
        }
        moveList[endOfList] = null;
        return moveList;
    }

    public MoveType[] generateMoveList() {
        return generateMoveList(this.playersTurn);
    }

    private String moveListToString() {
        MoveType[] moves = generateMoveList();
        int counter = 0;
        String listString = "";
        while (moves[counter] != null) {
            Field mf = ((Move) moves[counter]).getField();
            Field cf = ((Move) moves[counter]).getCaptureField();

            make(moves[counter]);
            listString = listString + (counter % 5 == 0 ? "\n" : "\t") + (counter + 1)
                    + ": ("
                    + mf.getX()
                    + ","
                    + mf.getY()
                    + ")" + (cf != null ? "x" + cf.getX() + "," + cf.getY() : "")
                    + "="
                    + eval();
            undoMove();
            counter++;
        }
        return listString;
    }

    public BoardType getBoard() {
        return board;
    }

    private void enableFieldButtons(boolean b) {
        for (int n = 0; n < board.map.numberOfFields; n++) {
            if (board.get(n) == 0) {
                fieldButton[n].setEnabled(b);
            }
        }
    }

    private void stopGame() {
        enableFieldButtons(false);
        this.gameRunning = false;
        threadRunning = false;

        this.statistics.setHeader(
                "Statistics:\n"
                + this.playerBlack.toString()
                + "\n"
                + this.playerWhite.toString());
        LogWriter.println(this.statistics);

        if ((this.autoplay) && (this.statistics.getGameCounter() < 100)) {
            resetButtons();
            newGame();
        } else {
            this.stopGameItem.setEnabled(false);
        }
    }

    private void resetButtons() {
        for (int i = 0; i < board.map.numberOfFields; i++) {
            fieldButton[i].setEnabled(true);
            fieldButton[i].setIcon(fieldIcon);
            fieldButton[i].setRolloverIcon(fieldRolloverIcon);
            fieldButton[i].setPressedIcon(fieldPressedIcon);
            fieldButton[i].setDisabledIcon(fieldIcon);
        }
    }

    private void updateGUI(Move move) {
        Field moveField = move.getField();
        Field captureField = move.getCaptureField();
        int colour = move.getMarble().getColour();
        if (moveField != null) {
            int n = board.map.getNumber(moveField);
            Icon marbleIcon = colour == 1 ? blackMarbleIcon : whiteMarbleIcon;
            fieldButton[n].setDisabledIcon(marbleIcon);
            fieldButton[n].setEnabled(false);
            fieldButton[n].repaint();
            if (this.noEntryField != null) {
                fieldButton[board.map.getNumber(this.noEntryField)].setEnabled(true);
                this.noEntryField = null;
            }
            if (captureField != null) {
                n = board.map.getNumber(captureField);
                fieldButton[n].setIcon(fieldIcon);
                fieldButton[n].setRolloverIcon(fieldRolloverIcon);
                fieldButton[n].setPressedIcon(fieldPressedIcon);
                fieldButton[n].setDisabledIcon(fieldIcon);
                fieldButton[n].setEnabled(false);
                fieldButton[n].repaint();
                this.noEntryField = captureField;
            }
        }
    }

    private void writeMove(Move move) {
        this.statistics.add(move);
        Field moveField = move.getField();
        if ((moveField == null) || (move.getMarble() == null)) {
            return;
        }
        int moveFieldNumber = board.map.getNumber(moveField);
        Field captureField = move.getCaptureField();
        int colour = move.getMarble().getColour();
        int moveNo = (this.nextMovePointer + 1) / 2;
        String moveStr = moveFieldNumber + 1 + (captureField == null ? "" : new StringBuffer("x").append(board.map.getNumber(captureField) + 1).toString());
        String tmpInfoStr = "";
        if ((colour == 1) && (this.playerBlack.isComputer())) {
            tmpInfoStr = tmpInfoStr + ((AlphaBeta) this.playerBlack.getEngine()).keepIndex;
        } else if ((colour == -1) && (this.playerWhite.isComputer())) {
            tmpInfoStr = tmpInfoStr + ((AlphaBeta) this.playerWhite.getEngine()).keepIndex;
        }
        String outStr = (colour == 1 ? "  " + moveNo + ".\t" : "") + moveStr + (colour == 1 ? "\t" : "\n");

        if (LogWriter.isPrintModus(1)) {
            LogWriter.out.print(outStr);
        } else {
            LogWriter.out.println((colour == 1 ? "Black's " + moveNo + ". move: " : new StringBuffer("White's ").append(moveNo).append(". move: ").toString()) + moveStr);
        }

        historyNotePad.append(outStr);
        if (board.won(colour)) {
            this.statistics.finaliseGame(colour);
            if (colour == 1) {
                historyNotePad.append("\n\t   1 - 0\n");
                LogWriter.out.println("\n\t   1 - 0\n");
            } else {
                historyNotePad.append("\n\t   0 - 1\n");
                LogWriter.out.println("\n\t   0 - 1\n");
            }
        }
        internalBoardPane.setText(board.toString());
        if (this.gameRunning) {
            moveListBoardPane.setText(moveListToString());
        }
    }

    class Controller implements ActionListener {
        Controller() {
        }

        public void actionPerformed(ActionEvent event) {
            int fieldNumber = Integer.parseInt(event.getActionCommand());
            int colour = Boku.this.getNextTurn();

            if (Boku.captureMove) {

                Boku.this.noEntryField = Boku.board.map.getField(fieldNumber);

                Boku.this.enableFieldButtons(true);

                int counter = 0;

                while (Boku.captureFields[counter] != null) {
                    int n = Boku.board.map.getNumber(Boku.captureFields[counter]);
                    Boku.fieldButton[n].setEnabled(false);
                    Boku.captureFields[counter] = null;
                    counter++;
                }
                Move move = new Move(Boku.moveField, Boku.this.noEntryField, new Marble(colour));
                Boku.this.make(move);
                Boku.this.writeMove(move);

                Boku.fieldButton[fieldNumber].setIcon(Boku.fieldIcon);
                Boku.fieldButton[fieldNumber].setRolloverIcon(Boku.fieldRolloverIcon);
                Boku.fieldButton[fieldNumber].setPressedIcon(Boku.fieldPressedIcon);
                Boku.fieldButton[fieldNumber].setDisabledIcon(Boku.fieldIcon);
                Boku.fieldButton[fieldNumber].setEnabled(false);
                fieldNumber = -1;
                Boku.captureMove = false;

                new Boku.EngineThread().start();

            } else {
                Boku.moveField = Boku.board.map.getField(fieldNumber);

                Marble tmpMarble = new Marble(colour);
                Boku.board.set(tmpMarble, Boku.moveField);
                Boku.this.updateGUI(new Move(Boku.moveField, tmpMarble));

                Boku.winnerFields = Boku.board.getWinnerFields(Boku.moveField, colour);
                if (Boku.winnerFields[0] != null) {
                    Move winnerMove = new Move(Boku.moveField, new Marble(colour));
                    Boku.this.make(winnerMove);
                    Boku.this.writeMove(winnerMove);
                    Boku.this.updateGUI(winnerMove);

                    for (int i = 0; i < Boku.winnerFields.length; i++) {
                        int n = Boku.board.map.getNumber(Boku.winnerFields[i]);
                        Boku.fieldButton[n].setDisabledIcon(colour == 1 ? Boku.blackMarblePressedIcon : Boku.whiteMarblePressedIcon);
                    }
                    JOptionPane.showMessageDialog(null,
                            "Congratulations!" + Boku.CR + (colour == 1 ? "Black" : "White") + " won.",
                            "Game over",
                            -1);
                    Boku.this.stopGame();
                } else {
                    Boku.captureFields = Boku.board.getCaptureFields(Boku.moveField, colour);

                    if (Boku.captureFields[0] != null) {

                        Boku.this.enableFieldButtons(false);

                        int counter = 0;
                        while (Boku.captureFields[counter] != null) {
                            int n = Boku.board.map.getNumber(Boku.captureFields[counter]);
                            if (colour == -1) {
                                Boku.fieldButton[n].setIcon(Boku.blackMarbleCaptureIcon);
                                Boku.fieldButton[n].setRolloverIcon(Boku.blackMarbleRolloverIcon);
                                Boku.fieldButton[n].setPressedIcon(Boku.blackMarblePressedIcon);
                            } else {
                                Boku.fieldButton[n].setIcon(Boku.whiteMarbleCaptureIcon);
                                Boku.fieldButton[n].setRolloverIcon(Boku.whiteMarbleRolloverIcon);
                                Boku.fieldButton[n].setPressedIcon(Boku.whiteMarblePressedIcon);
                            }
                            Boku.fieldButton[n].setEnabled(true);
                            counter++;
                        }
                        if (Boku.this.options.showCaptureMessage()) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Please click on the marble you want to capture.",
                                    "Sandwich!",
                                    1);
                        }
                        Boku.captureMove = true;
                    } else {
                        Move move = new Move(Boku.moveField, new Marble(colour));
                        Boku.this.make(move);
                        Boku.this.writeMove(move);
                        Boku.this.updateGUI(move);
                        if (Boku.this.isDraw()) {
                            return;
                        }
                        new Boku.EngineThread().start();
                    }
                }
            }
        }
    }

    class EngineThread extends Thread {

        public EngineThread() {
            Boku.this.stopGameItem.setEnabled(true);
        }

        public void run() {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException localInterruptedException) {
            }
            Boku.threadRunning = true;

            while ((Boku.this.gameRunning) && (Boku.threadRunning) && (Boku.this.nextPlayer().isComputer())) {
                Boku.this.enableFieldButtons(false);
                EngineType searchEngine = Boku.this.nextPlayer().getEngine();
                Boku.board.setLookupTable(Boku.this.nextPlayer().getLookupTable());
                int colour = Boku.this.getNextTurn();
                if (Boku.this.isDraw()) {
                    return;
                }
                MoveType[] moves = Boku.this.generateMoveList();

                int[] myMoves = searchEngine.calculateMove(colour, moves);
                if (!Boku.threadRunning) {
                    break;
                }

                Move computerMove = (Move) moves[myMoves[0]];

                if (computerMove == null) {
                    throw new NullPointerException("no computer move");
                }
                if (computerMove.getMarble() == null) {
                    throw new NullPointerException("no marble");
                }
                if (computerMove.getField() == null) {
                    throw new NullPointerException("no field");
                }
                Boku.board.set(computerMove.getMarble(), computerMove.getField());

                Boku.winnerFields = Boku.board.getWinnerFields(computerMove.getField(), colour);
                if (Boku.winnerFields[0] != null) {
                    Move winnerMove = new Move(computerMove.getField(), new Marble(colour));
                    Boku.this.make(winnerMove);
                    Boku.this.writeMove(winnerMove);
                    Boku.this.updateGUI(winnerMove);

                    for (int i = 0; i < Boku.winnerFields.length; i++) {
                        int n = Boku.board.map.getNumber(Boku.winnerFields[i]);
                        Boku.fieldButton[n].setDisabledIcon(colour == 1 ? Boku.blackMarblePressedIcon : Boku.whiteMarblePressedIcon);
                    }
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException localInterruptedException1) {
                    }
                    if (!Boku.this.autoplay) {
                        JOptionPane.showMessageDialog(
                                null,
                                (colour == 1 ? "Black" : "White")
                                + " won.",
                                "Game over",
                                -1);
                    }
                    Boku.this.stopGame();
                    return;
                }

                Field captureField = computerMove.getCaptureField();
                if (captureField != null) {

                    if ((!Boku.this.autoplay) && (Boku.this.options.showCaptureMessage())) {
                        Move tmpMove = new Move(computerMove.getField(), computerMove.getMarble());
                        Boku.this.updateGUI(tmpMove);
                        int n = Boku.board.map.getNumber(captureField);
                        if (colour == -1) {
                            Boku.fieldButton[n].setDisabledIcon(Boku.blackMarbleCaptureIcon);
                        } else {
                            Boku.fieldButton[n].setDisabledIcon(Boku.whiteMarbleCaptureIcon);
                        }
                        Thread.yield();
                        JOptionPane.showMessageDialog(null,
                                "Computer will take a marble off the board.",
                                "Sandwich!",
                                1);
                    }
                }

                Boku.this.make(computerMove);
                Boku.this.writeMove(computerMove);

                Boku.this.enableFieldButtons(true);
                Boku.this.updateGUI(computerMove);

                Thread.yield();
            }
        }
    }

    private boolean isDraw() {
        boolean isDraw = false;
        MoveType[] moves = generateMoveList();

        if ((moves == null) || (moves[0] == null)) {
            if (!this.autoplay) {
                JOptionPane.showMessageDialog(null, "The game is a draw.",
                        "Game over",
                        -1);
            }
            historyNotePad.append("\n\t  0.5 - 0.5\n");
            LogWriter.out.println("\n\t  0.5 - 0.5\n");
            this.statistics.finaliseGame(0);
            stopGame();
            isDraw = true;

            threadRunning = false;
        }

        return isDraw;
    }

    public String getAppletInfo() {
        return aboutString;
    }

    public boolean isApplet() {
        return isApplet;
    }

    public String[][] getParameterInfo() {
        String[][] paramInfo
                = {
                    {
                        "1. Parameter: h",
                        "values: 1 to (14-d)",
                        "the number of fields in the top (or \nbottom) horizontal line."},
                    {
                        "2. Parameter: d",
                        "values: 1 to (14-h)",
                        "the number of fields in the outside \ndiagonal lines.\n\nIf no parameters are supplied Boku starts with standard board size \n(h = 5, d = 6)"}};

        return paramInfo;
    }

    private static void parseParameters(String hString, String dString) {
        String RuntimeErrorMsg
                = "Wrong parameters. Cannot start Boku. Please try again.";
        try {
            if ((hString == null) && (dString == null)) {
                h = 5;
                d = 6;
            } else {
                h = Integer.parseInt(hString);
                d = Integer.parseInt(dString);
            }
            if (h < 1) {
                throw new IllegalArgumentException(
                        "The horizontal length\nmust be at least 1 field(s).");
            }

            if (d < 1) {
                throw new IllegalArgumentException(
                        "The diagonal length\nmust be at least 1 field(s).");
            }

            if (h + d - 1 > 13) {
                throw new IllegalArgumentException(
                        "The maximum length of a line on the board\nmust not exceed 13 fields.\nmax length = h + d -1 = " + (h + d - 1)
                        + "\n");
            }
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(
                    null,
                    "Invalid parameters.\nh = "
                    + hString
                    + "\n"
                    + "d = "
                    + dString
                    + "\n",
                    "Wrong parameter",
                    0);
            if (!isApplet) {
                System.err.println(RuntimeErrorMsg);
                System.exit(-1);
            } else {
                throw new RuntimeException(RuntimeErrorMsg);
            }
        } catch (IllegalArgumentException ie) {
            JOptionPane.showMessageDialog(
                    null,
                    ie.getMessage(),
                    "Wrong parameter",
                    0);
            if (!isApplet) {
                System.err.println(RuntimeErrorMsg);
                System.exit(-1);
            } else {
                throw new RuntimeException(RuntimeErrorMsg);
            }
        }
    }

    public static void main(String[] args) {
        isApplet = false;

        String hString = args.length > 0 ? args[0] : null;
        String dString = args.length > 1 ? args[1] : null;
        parseParameters(hString, dString);

        JFrame window = new JFrame("Boku");

        Boku boku = new Boku();

        boku.init();
        boku.start();

        window.getContentPane().add(boku);
        window.setSize(defaultDimension);
        window.setResizable(false);
        window.setVisible(true);
        window.setDefaultCloseOperation(3);
    }
}


/* Location:              /home/alexandre/Boku1.21.jar!/boku/Boku.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
