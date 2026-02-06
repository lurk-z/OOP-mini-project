import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
public class MainDashboard extends JFrame {
    private JPanel sidebarPanel;
    private JPanel contentPanel;

    // โมเดลของตารางต่างๆในโปรแกรมสร้างไว้ตรงนี้จะได้เข้าถึงได้ทุกที่
    private DefaultTableModel movieModel;
    private DefaultTableModel scheduleModel;
    private DefaultListModel<String> dateListModel;

    public MainDashboard(){
        // เปิดการเชื่อมต่อ Database
        try {
            DatabaseManager.getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database connection error: " + e.getMessage());
        }

        setTitle("Wig Shop Admin Panel");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);//คำสั่งจัดกลางจอ
        setLayout(new BorderLayout());//ใช้ Layout แบบ Border

        initSidebar();
        initContent();

        setVisible(true);
    }
// Sidebar บวก ปุ่มๆ
    private void initSidebar(){
        sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(250, 0));
        sidebarPanel.setBackground(new Color(20, 30, 45));
        sidebarPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        JLabel logoLabel = new JLabel("ADMIN PANEL");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(new EmptyBorder(20, 0, 20, 0));

        JButton btnMovie = createMenuButton("Movie");
        JButton btnSchedule = createMenuButton("Schedule");
        JButton btnReport = createMenuButton("Report");

        // ActionListener สำหรับปุ่มมมมมมม
        btnMovie.addActionListener(e -> {
            showMoviePage();
        });
        btnSchedule.addActionListener(e -> {
            showSchedulePage();
        });
        btnReport.addActionListener(e -> {
            showReportPage();
        });
        sidebarPanel.add(logoLabel);
        sidebarPanel.add(btnMovie);
        sidebarPanel.add(btnSchedule);
        sidebarPanel.add(btnReport);

        this.add(sidebarPanel, BorderLayout.WEST);

    }
// กระดาษขาวไว้วาดหน้าต่างๆใส่
    private void initContent(){
        contentPanel = new JPanel();
        contentPanel.setBackground(new Color(25, 35, 55));
        contentPanel.setLayout(new BorderLayout());
        showWelcomPage();

        this.add(contentPanel, BorderLayout.CENTER);
    }
// หน้าแรก
    private void showWelcomPage(){
        contentPanel.removeAll();

        JPanel welcomPanel = new JPanel(new GridBagLayout());
        welcomPanel.setBackground(new Color(25, 35, 55));

        JLabel text = new JLabel("Welcome To Ticket Manager Program");
        text.setFont(new Font("Segoe UI", Font.BOLD, 24));
        text.setForeground((Color.GRAY));

        welcomPanel.add(text);
        contentPanel.add(welcomPanel, BorderLayout.CENTER);
        refreshScreen();
    }
// หน้า Movie
    private  void showMoviePage(){
        contentPanel.removeAll();

        JPanel moviePanel = new JPanel(new BorderLayout());
        moviePanel.setBackground(new Color(25, 35, 55));
        
        String[] cols = {"ID", "Title","Category", "Price"};
        movieModel = new DefaultTableModel(cols, 0);
        // โหลดข้อมูลจาก Database ทุกครั้ง
        for (Product product : ProductManager.getAllProducts()) {
            movieModel.addRow(new Object[]{product.getId(), product.getTitle(), product.getCategory(), product.getPrice()});
        }
        
        JTable table = new JTable(movieModel);
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scroll = new JScrollPane(table);
        moviePanel.add(scroll, BorderLayout.CENTER);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 35, 55));
        header.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("Movie Manager");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton btnAdd = new JButton("+ Add new movie");
        btnAdd.setBackground(new Color(46, 204, 113)); // สีเขียว
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.addActionListener(e -> {showAddMovieDialog();});

        JButton btnDelete = new JButton("- Delete");
        btnDelete.setBackground(new Color(231, 76, 60)); // แดง
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1){
                JOptionPane.showMessageDialog(null, "Select a movie to delete");
            } else {
                int confirm = JOptionPane.showConfirmDialog(null, "Delete this movie?", "Yes", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String id = movieModel.getValueAt(selectedRow, 0).toString();
                    ProductManager.deleteProduct(id);
                    movieModel.removeRow(selectedRow);
                }
            }
        });
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        header.add(buttonPanel, BorderLayout.EAST);
        moviePanel.add(header, BorderLayout.NORTH);

        contentPanel.add(moviePanel, BorderLayout.CENTER);
        refreshScreen();
    }
// หน้า Dialog ของ Movie (ตอนกดปุ่ม Add movie)
    private void showAddMovieDialog(){
        JDialog dialog = new JDialog(this, "Add new movie", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));

        JTextField txtTitle = new JTextField();
        String[] categories = {"Action", "Drama", "Comedy", "Sci-Fi", "Horror"}; 
        JComboBox<String> cbCategory = new JComboBox<>(categories);
        JTextField txtPrice = new JTextField();

        dialog.add(new JLabel("Title:"));
        dialog.add(txtTitle);
        dialog.add(new JLabel("Category:"));
        dialog.add(cbCategory);
        dialog.add(new JLabel("Price:"));
        dialog.add(txtPrice);

        JButton btnSave = createMenuButton("Save");
        dialog.add(new JLabel(""));
        dialog.add(btnSave);
        btnSave.addActionListener((e ->{
            String title = txtTitle.getText();
            String cat = cbCategory.getSelectedItem().toString();
            String price = txtPrice.getText();

            if (title.isEmpty() || price.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields");
                return;
            }

            try {
                String id = String.valueOf(System.currentTimeMillis()).substring(8);
                ProductManager.addProduct(id, title, cat, Double.parseDouble(price));
                movieModel.addRow((new Object[]{id , title, cat, price}));
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid price");
            }
        }));

        dialog.setVisible(true);
    }
// หน้า Schedule
    private void showSchedulePage() {
        contentPanel.removeAll();

        JPanel mainLayout = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(200, 0));
        leftPanel.setBackground(new Color(20 ,30, 45));
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblDate = new JLabel("Select Date");
        lblDate.setForeground(Color.WHITE);
        lblDate.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDate.setBorder(new EmptyBorder(0, 0, 10, 0));
        leftPanel.add(lblDate, BorderLayout.NORTH);

        dateListModel = new DefaultListModel<>();
        // โหลดวันที่จาก Database
        for (DateModel date : DateManager.getAllDates()) {
            dateListModel.addElement(date.getDateStr());
        }
        JList<String> listDate = new JList<>(dateListModel);
        
        listDate.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        listDate.setBackground(new Color(30, 40, 60)); // สีพื้น
        listDate.setForeground(Color.WHITE); // สีตัวหนังสือ
        listDate.setFixedCellHeight(40); // ความสูงบรรทัด
        listDate.setSelectionBackground(new Color(230, 50, 100)); // สีตอนเลือก
        
        leftPanel.add(new JScrollPane(listDate), BorderLayout.CENTER);

        JPanel btnPanelLeft = new JPanel(new GridLayout(1, 2, 5, 0));
        btnPanelLeft.setOpaque(false);
        btnPanelLeft.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton btnAddDate = new JButton("Add date");
        btnAddDate.setBackground(new Color(46, 204, 113));
        btnAddDate.setForeground(Color.WHITE);
        btnAddDate.setFocusPainted(false);
        btnAddDate.setBorder(new EmptyBorder(10, 0, 10, 0));
        btnAddDate.addActionListener(e -> showAddDateDialog());
        JButton btnDelDate = new JButton("- Delete");
        btnDelDate.setBackground(new Color(231, 76, 60)); // แดง
        btnDelDate.setForeground(Color.WHITE);
        
        btnDelDate.addActionListener(e -> {
            // เช็คว่าเลือกวันไหนอยู่
            String selectedDate = listDate.getSelectedValue();
            if (selectedDate != null) {
                int dateId = DateManager.getDateId(selectedDate);
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this date?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    DateManager.deleteDate(dateId);
                    dateListModel.removeElement(selectedDate);
                    scheduleModel.setRowCount(0);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Select a date to delete");
            }
        });

        // ยัดปุ่มใส่กล่อง
        btnPanelLeft.add(btnAddDate);
        btnPanelLeft.add(btnDelDate);
        leftPanel.add(btnPanelLeft, BorderLayout.SOUTH);
        // --- 3. ด้านขวา: ตารางรอบฉาย (Schedule Table) ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(25, 35, 55)); // สีพื้นขวา
        
        // Header ของขวา (ชื่อวันที่ที่เลือก + ปุ่มเพิ่มรอบ)
        JPanel headerRight = new JPanel(new BorderLayout());
        headerRight.setBackground(new Color(25, 35, 55));
        headerRight.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel lblSelectedDate = new JLabel("Date: Not selected yet");
        lblSelectedDate.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblSelectedDate.setForeground(Color.WHITE);

        // ตารางรอบฉาย
        String[] cols = {"Time", "Movie", "Theater", "Price"};
        scheduleModel = new DefaultTableModel(cols, 0);
        JTable tableSchedule = new JTable(scheduleModel);
        tableSchedule.setRowHeight(40);
        JPanel btnPanelRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanelRight.setOpaque(false);
        
        JButton btnAddShow = new JButton("+ Add new round");
        btnAddShow.setBackground(new Color(46, 204, 113)); // สีเขียว
        btnAddShow.setForeground(Color.WHITE);
        btnAddShow.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddShow.addActionListener(e -> {
            String selectedDate = listDate.getSelectedValue();
            if (selectedDate == null){
                JOptionPane.showMessageDialog(this, "Please select the date");
            } else {
                int dateId = DateManager.getDateId(selectedDate);
                showAddShowtimeDialog(dateId);
            }
        });

        JButton btnDelShow = new JButton("- Delete");
        btnDelShow.setBackground(new Color(231, 76, 60));
        btnDelShow.setForeground(Color.WHITE);
        btnDelShow.addActionListener(e -> {
            int row = tableSchedule.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Select a showtime");
            } else {
                int confirm = JOptionPane.showConfirmDialog(null, "Delete this showtime?", "Yes", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // หา Schedule ID จากตาราง (ต้องเพิ่ม ID เป็นคอลัมน์ที่ 0)
                    int scheduleId = -1;
                    // ค้นหาจาก data
                    String selectedDate = listDate.getSelectedValue();
                    if (selectedDate != null) {
                        int dateId = DateManager.getDateId(selectedDate);
                        String time = tableSchedule.getValueAt(row, 0).toString();
                        for (Schedule schedule : ScheduleManager.getSchedulesByDate(dateId)) {
                            if (schedule.getTime().equals(time)) {
                                scheduleId = schedule.getId();
                                break;
                            }
                        }
                    }
                    if (scheduleId != -1) {
                        ScheduleManager.deleteSchedule(scheduleId);
                    }
                    scheduleModel.removeRow(row);
                }
            }
        });
        btnPanelRight.add(btnAddShow);
        btnPanelRight.add(btnDelShow);
        headerRight.add(btnPanelRight, BorderLayout.EAST);
        headerRight.add(lblSelectedDate, BorderLayout.WEST);
        
        
        
        rightPanel.add(headerRight, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(tableSchedule), BorderLayout.CENTER);

        // --- 4. Logic การเชื่อมโยง (กดซ้าย เปลี่ยนขวา) ---
        listDate.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // กัน Event รันซ้ำ
                String selected = listDate.getSelectedValue();
                if (selected == null) return;
                lblSelectedDate.setText("Date: " + selected);
                
                // โหลดข้อมูลจาก Database ตามวันที่ที่เลือก
                scheduleModel.setRowCount(0);
                int dateId = DateManager.getDateId(selected);
                for (Schedule schedule : ScheduleManager.getSchedulesByDate(dateId)) {
                    Product product = ProductManager.getProduct(schedule.getProductId());
                    String movieTitle = (product != null) ? product.getTitle() : "Unknown";
                    scheduleModel.addRow(new Object[]{schedule.getTime(), movieTitle, schedule.getTheater(), schedule.getPrice()});
                }
            }
        });

        mainLayout.add(leftPanel, BorderLayout.WEST);
        mainLayout.add(rightPanel, BorderLayout.CENTER);

        contentPanel.add(mainLayout, BorderLayout.CENTER);
        refreshScreen();

    }
// หน้า Dialog ของ Schedule (ตอนกดปุ่ม Add Schedule)
private void showAddShowtimeDialog(int dateId) {
    // 1. สร้างหน้าต่าง
    JDialog dialog = new JDialog(this, "Add showtime", true);
    dialog.setSize(400, 350);
    dialog.setLocationRelativeTo(this);
    dialog.setLayout(new GridLayout(5, 2, 10, 20)); // 5 แถว

    // 2. สร้างตัวเลือก (Components)
    
    // [ไฮไลท์] Dropdown เลือกหนัง (ดึงจาก Database)
    JComboBox<Product> cbMovie = new JComboBox<>();
    List<Product> products = ProductManager.getAllProducts();
    for (Product product : products) {
        cbMovie.addItem(product);
    }
    
    if (products.isEmpty()) {
        cbMovie.addItem(new Product("", "No movies available", "", 0));
    }

    // Dropdown เลือกโรง
    String[] theaters = {"Theater 1", "Theater 2", "Theater 3"};
    JComboBox<String> cbTheater = new JComboBox<>(theaters);

    // ช่องกรอกเวลา
    JTextField txtTime = new JTextField("10:30"); // ใส่ค่า Default ไว้หน่อย

    // ช่องราคา (ดึง Auto จากหนังที่เลือก)
    JTextField txtPrice = new JTextField("200");

    // 3. ยัดของใส่ Dialog
    dialog.add(new JLabel("  Movie:"));
    dialog.add(cbMovie);
    
    dialog.add(new JLabel("  Theater:"));
    dialog.add(cbTheater);
    
    dialog.add(new JLabel("  Time:"));
    dialog.add(txtTime);

    dialog.add(new JLabel("  Price:"));
    dialog.add(txtPrice);

    // 4. ปุ่มบันทึก
    JButton btnSave = new JButton("Save");
    btnSave.setBackground(new Color(46, 204, 113)); // สีเขียว
    btnSave.setForeground(Color.WHITE);
    
    btnSave.addActionListener(e -> {
        // ดึงค่าที่เลือก
        Product selectedProduct = (Product) cbMovie.getSelectedItem();
        String theater = cbTheater.getSelectedItem().toString();
        String time = txtTime.getText();
        String price = txtPrice.getText();
        
        if (selectedProduct == null || selectedProduct.getId().isEmpty() || time.isEmpty() || price.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Please fill in all fields");
            return;
        }
        
        try {
            double priceValue = Double.parseDouble(price);
            int scheduleId = ScheduleManager.addSchedule(dateId, time, selectedProduct.getId(), theater, priceValue);
            if (scheduleId != -1) {
                scheduleModel.addRow(new Object[]{time, selectedProduct.getTitle(), theater, price});
                dialog.dispose();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Invalid price");
        }
    });

    dialog.add(new JLabel("")); // ช่องว่าง
    dialog.add(btnSave);

    dialog.setVisible(true);
}
// หน้า Report
private void showReportPage(){
    contentPanel.removeAll();

    JPanel mainLayout = new JPanel();
    mainLayout.setLayout(new BoxLayout(mainLayout, BoxLayout.Y_AXIS));
    mainLayout.setBackground(new Color(25, 35, 55));
    mainLayout.setBorder(new EmptyBorder(20, 20, 20, 20));

    JPanel cardPanel = new JPanel(new GridLayout(1, 3, 20, 0));
    cardPanel.setBackground(new Color(25, 35, 55));
    cardPanel.setMaximumSize(new Dimension(2000, 120));

    double totalRevenue = TransactionManager.getTotalRevenue();
    int ticketsSold = TransactionManager.getTotalTicketsSold();
    String bestSeller = TransactionManager.getBestSellingMovie();

    cardPanel.add(createCard("Total Revenue", "฿ " + String.format("%.2f", totalRevenue), new Color(46, 204, 113)));
    cardPanel.add(createCard("Tickets Sold", String.valueOf(ticketsSold), new Color(52, 152, 219)));
    cardPanel.add(createCard("Best Seller", bestSeller, new Color(230, 50, 100)));

    mainLayout.add(cardPanel);
    mainLayout.add(Box.createRigidArea(new Dimension(0, 30))); // เว้นระยะห่างแนวตั้ง

    // --- ส่วนที่ 2: Simple Bar Chart (ยอดขายแยกตามหนัง) ---
    JPanel chartPanel = new JPanel(new BorderLayout());
    chartPanel.setBackground(new Color(30, 40, 60)); // พื้นหลังเข้มกว่าปกติหน่อย
    chartPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
    
    JLabel lblChart = new JLabel("Top movies");
    lblChart.setFont(new Font("Segoe UI", Font.BOLD, 18));
    lblChart.setForeground(Color.WHITE);
    lblChart.setBorder(new EmptyBorder(0, 0, 15, 0));
    chartPanel.add(lblChart, BorderLayout.NORTH);
    // Top movies from database
    JPanel bars = new JPanel(new GridLayout(0, 1, 0, 10));
    bars.setOpaque(false);

    List<MovieSales> topMovies = TransactionManager.getTopSellingMovies(4);
    if (topMovies.isEmpty()) {
        JLabel emptyLabel = new JLabel("No sales yet");
        emptyLabel.setForeground(Color.LIGHT_GRAY);
        bars.add(emptyLabel);
    } else {
        int maxCount = 1;
        for (MovieSales ms : topMovies) {
            if (ms.getCount() > maxCount) {
                maxCount = ms.getCount();
            }
        }
        Color[] colors = {
            new Color(46, 204, 113),
            new Color(52, 152, 219),
            new Color(155, 89, 182),
            new Color(231, 76, 60)
        };
        int i = 0;
        for (MovieSales ms : topMovies) {
            int percent = (int) Math.round((ms.getCount() * 100.0) / maxCount);
            String label = ms.getTitle() + " (" + ms.getCount() + ")";
            bars.add(createBarItem(label, percent, colors[i % colors.length]));
            i++;
        }
    }

    chartPanel.add(bars, BorderLayout.CENTER);
    mainLayout.add(chartPanel);
    mainLayout.add(Box.createRigidArea(new Dimension(0, 30))); // เว้นระยะ

    // --- ส่วนที่ 3: ตารางรายการล่าสุด (Transaction) ---
    JLabel lblTable = new JLabel("Recent Transactions");
    lblTable.setFont(new Font("Segoe UI", Font.BOLD, 16));
    lblTable.setForeground(Color.LIGHT_GRAY);
    lblTable.setAlignmentX(Component.LEFT_ALIGNMENT); // ชิดซ้าย
    mainLayout.add(lblTable);
    mainLayout.add(Box.createRigidArea(new Dimension(0, 10)));
    
    String[] cols = {"Time", "Movie", "Seat", "Price"};
    DefaultTableModel tableModel = new DefaultTableModel(cols, 0);
    for (ReportTransaction t : TransactionManager.getRecentTransactions(10)) {
        tableModel.addRow(new Object[]{
            t.getTimestamp(),
            t.getMovieTitle(),
            t.getSeat(),
            String.format("%.2f", t.getPrice())
        });
    }

    JTable table = new JTable(tableModel);
    table.setRowHeight(30);
    JScrollPane scroll = new JScrollPane(table);
    scroll.setPreferredSize(new Dimension(0, 150)); // กำหนดความสูงตาราง
    
    mainLayout.add(scroll);

    contentPanel.add(mainLayout, BorderLayout.CENTER);
    refreshScreen();
}
// func เอาไว้สร้างการ์ด
private JPanel createCard(String title, String value, Color color) {
    JPanel card = new JPanel(new BorderLayout());
    card.setBackground(color); // สีพื้นหลังตามที่ส่งมา
    card.setBorder(new EmptyBorder(15, 20, 15, 20));
    
    JLabel lblTitle = new JLabel(title);
    lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    lblTitle.setForeground(new Color(255, 255, 255, 200)); // ขาวโปร่งแสง
    
    JLabel lblValue = new JLabel(value);
    lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
    lblValue.setForeground(Color.WHITE);
    
    card.add(lblTitle, BorderLayout.NORTH);
    card.add(lblValue, BorderLayout.CENTER);
    
    return card;
}
// func ไว้สร้างกราฟ
private JPanel createBarItem(String name, int percent, Color color) {
    JPanel p = new JPanel(new BorderLayout(10, 0));
    p.setOpaque(false);
    
    JLabel lblName = new JLabel(name);
    lblName.setForeground(Color.WHITE);
    lblName.setPreferredSize(new Dimension(100, 0)); // จองพื้นที่ชื่อหนัง 100px
    
    JProgressBar bar = new JProgressBar(0, 100);
    bar.setValue(percent);
    bar.setStringPainted(true); // โชว์ตัวเลข %
    bar.setForeground(color); // สีแท่งกราฟ
    bar.setBackground(new Color(255, 255, 255, 50)); // สีพื้นหลังจางๆ
    bar.setBorderPainted(false);
    
    p.add(lblName, BorderLayout.WEST);
    p.add(bar, BorderLayout.CENTER);
    
    return p;
}
// func เอาไว้ใช้รีหน้าจอวาดใหม่
    private void  refreshScreen(){
        contentPanel.revalidate();
        contentPanel.repaint();
    }
// func เอาไว้สร้างปุ่มตรงเมนูนะจ๊ะ
    private JButton createMenuButton(String text){
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(200, 45)); // ขนาดปุ่ม
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 40, 60));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // เมาส์เป็นรูปมือ
        
        // แถม: Hover Effect (เอาเมาส์ชี้แล้วเปลี่ยนสี)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(230, 50, 100)); // สีชมพูเมื่อชี้
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(30, 40, 60)); // สีเดิมเมื่อออก
            }
        });
        
        return btn;
    }
// หน้า Dialog ของ Date (ตอนกดปุ่ม Add date)
    private void showAddDateDialog() {
        JDialog dialog = new JDialog(this, "Select Date", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // ส่วนกลาง - เลือกวันที่
        JPanel centerPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblDay = new JLabel("Day:");
        JSpinner spinDay = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        spinDay.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblMonth = new JLabel("Month:");
        JSpinner spinMonth = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        spinMonth.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblYear = new JLabel("Year:");
        JSpinner spinYear = new JSpinner(new SpinnerNumberModel(2026, 2020, 2050, 1));
        spinYear.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblPreview = new JLabel("Preview: --/--/----");
        lblPreview.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPreview.setForeground(new Color(52, 152, 219));

        // Update preview เมื่อเลือก
        ChangeListener updatePreview = e -> {
            int day = (int) spinDay.getValue();
            int month = (int) spinMonth.getValue();
            int year = (int) spinYear.getValue();
            lblPreview.setText(String.format("Preview: %02d %02d %04d", day, month, year));
        };
        spinDay.addChangeListener(updatePreview);
        spinMonth.addChangeListener(updatePreview);
        spinYear.addChangeListener(updatePreview);

        centerPanel.add(lblDay);
        centerPanel.add(spinDay);
        centerPanel.add(lblMonth);
        centerPanel.add(spinMonth);
        centerPanel.add(lblYear);
        centerPanel.add(spinYear);
        centerPanel.add(new JLabel(""));
        centerPanel.add(lblPreview);

        dialog.add(centerPanel, BorderLayout.CENTER);

        // ส่วนล่าง - ปุ่ม
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JButton btnSave = new JButton("Add");
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.addActionListener(e -> {
            int day = (int) spinDay.getValue();
            int month = (int) spinMonth.getValue();
            int year = (int) spinYear.getValue();
            String newDate = String.format("%02d %02d %04d", day, month, year);

            int dateId = DateManager.addDate(newDate);
            if (dateId != -1) {
                dateListModel.addElement(newDate);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Date already exists");
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(149, 165, 166));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancel.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);

        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainDashboard();
        });
    }
}
