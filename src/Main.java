import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Date;
import java.sql.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {
    public static String oracle_user;
    private ArrayList<String[]> products;
    public static char[] oracle_password;
    public static ArrayList<String>addresses = new ArrayList<String>();
    public static class Customer {
        public String userName;
        public String password;
        public String ID;
        public String Address;

        public Customer(String userName, String password, String ID, String Address) {
            this.userName = userName;
            this.password = password;
            this.ID = ID;
            this.Address = Address;
        }

        // Getters and setters (optional)
        public void setAddress (String a)
        {
            this.Address = a;
        }
        public String getUserName() {
            return userName;
        }
        public void setUserName(String newName) {
            try {
                // Read the contents of the file
                File file = new File("customers.txt");
                BufferedReader br = new BufferedReader(new FileReader(file));
                ArrayList<String> lines = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
                br.close();

                // Update the customer's name in the lines list
                for (int i = 0; i < lines.size(); i++) {
                    String[] parts = lines.get(i).split(",");
                    if (parts.length >= 3 && parts[0].equals(userName)) {
                        parts[0] = newName;
                        lines.set(i, String.join(",", parts));
                        break; // Assuming username is unique, no need to continue searching
                    }
                }

                // Write the updated contents back to the file
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                for (String updatedLine : lines) {
                    bw.write(updatedLine);
                    bw.newLine();
                }
                bw.close();

                // Update the userName instance variable
                this.userName = newName;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String newPassword) {
            try {
                // Read the contents of the file
                File file = new File("customers.txt");
                BufferedReader br = new BufferedReader(new FileReader(file));
                ArrayList<String> lines = new ArrayList<>();
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
                br.close();

                // Update the customer's password in the lines list
                for (int i = 0; i < lines.size(); i++) {
                    String[] parts = lines.get(i).split(",");
                    if (parts.length >= 3 && parts[0].equals(userName)) {
                        parts[1] = newPassword;
                        lines.set(i, String.join(",", parts));
                        break; // Assuming username is unique, no need to continue searching
                    }
                }

                // Write the updated contents back to the file
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                for (String updatedLine : lines) {
                    bw.write(updatedLine);
                    bw.newLine();
                }
                bw.close();

                // Update the password instance variable
                this.password = newPassword;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getID() {
            return ID;
        }

        public String getAddress() {
            return Address;
        }
    }
    private static final String CUSTOMERS_FILE = "customers.txt";
    private static ArrayList<Customer> customerList = new ArrayList<>();

    public static class CustomerWindow extends JFrame {

        /*private String[][] products = {
                {String.format("%-5s", "012"), String.format("%-10s", "bananas"), String.format("%-15s", "enough stock"), String.format("%-7s", "12")},
                {String.format("%-5s", "013"), String.format("%-10s", "oranges"), String.format("%-15s", "enough stock"), String.format("%-7s", "25")},
                {String.format("%-5s", "014"), String.format("%-10s", "berries"), String.format("%-15s", "enough stock"), String.format("%-7s", "87")},
                {String.format("%-5s", "015"), String.format("%-10s", "avocado"), String.format("%-15s", "enough stock"), String.format("%-7s", "92")},
                {String.format("%-5s", "016"), String.format("%-10s", "redlime"), String.format("%-15s", "enough stock"), String.format("%-7s", "31")},
                {String.format("%-5s", "017"), String.format("%-10s", "grappes"), String.format("%-15s", "enough stock"), String.format("%-7s", "44")}
        };*/
        private ArrayList<String[]> products;
        private static ArrayList<String[]> comments;
        private static ArrayList<Integer[]> ratings;
        private Customer customer;
        private int recordCount = 0;
        private ArrayList<String> recordOrderAddress;
        private String currentOrderAddress;
        //private int[][] recordOrderAmount = new int[10][products.length];
        private ArrayList<ArrayList<Integer>> recordOrderAmount;
        private ArrayList<Integer> cartAmount;
        private ArrayList<Integer> currentOrderAmount;
        private ArrayList<String> details;
        //cart
        private JButton cartButton;
        private JPanel cartPanel;
        private JLabel cartLabel;
        private JButton orderButton;
        private JButton searchButton;
        private JPanel contentPanel;
        private JButton userButton;
        private JButton adminButton;
        private JButton sortButton;
        private JButton commentButton;

        public CustomerWindow(String title, int width, int height, Customer customer) throws SQLException
        {
            
            this.products = new ArrayList<>();
            this.recordOrderAmount = new ArrayList<>();
            this.recordOrderAddress = new ArrayList<>();
            this.recordCount = 0;
            this.customer = customer;
            this.details = new ArrayList<>();
            // Append the values to the products ArrayList
            String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
            String b_init;
            int a_init,c_init;
            Float d_init;
            try(Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password));)
            {
                int counter_init=  0;
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT Product.Product_ID, Product.Product_Name, SUM(Product_Stock.Amount), Product.Price  FROM Product_Stock LEFT OUTER JOIN Product on Product_Stock.Product_ID = Product.Product_ID GROUP BY Product.Product_ID,Product.Product_Name, Product.Price HAVING SUM(Product_Stock.Amount) > 0 ORDER BY Product.Product_ID");
                while(rs.next() && counter_init< 5)
                {
                    a_init= rs.getInt(1);
                    b_init= rs.getString(2);
                    c_init= rs.getInt(3);
                    d_init= rs.getFloat(4);
                    if( d_init>0)
                    {
                        String arr_init[] = {String.valueOf(a_init),b_init,String.valueOf(c_init),String.valueOf(d_init)};
                        products.add(arr_init);
                        counter_init++;
                    }
                }
                Statement stmt2 = conn.createStatement();
                ResultSet rs2 = stmt2.executeQuery("SELECT Product_ID, Product_Comment, Rating FROM Product_Comment");
                
                
                    
                    
                
                /*
                while(rs2.next())
                {
                    ratings.get(rs2.getInt(1)).add(rs2.getInt(3));
                    comments.get(rs2.getInt(1)).add(rs2.getString(2));
                }
                */
            }   

            
            this.currentOrderAmount = new ArrayList<>(Collections.nCopies(products.size(), 0));
            this.cartAmount = new ArrayList<>(Collections.nCopies(products.size(), 0));
            this.recordOrderAmount = new ArrayList<>();
            this.recordOrderAddress =  new ArrayList<>(10);
            setTitle(title);
            setSize(width, height);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create the main panel with a FlowLayout
            JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            setContentPane(mainPanel);

            // Create the panels and buttons
            cartButton = genBP(10,10,200,100, 75, 75,Color.RED,null ,"resources/cart-icon.jpeg");
            orderButton = genButton(230, 10, 100,100, 75, 75,Color.BLUE, null, "resources/order-icon.jpeg");
            searchButton = genButton(300, 10, 600, 100, 75, 75,Color.BLACK, "Press here to search for products..." , "resources/search-icon.jpeg");
            contentPanel = genPan(30,130, 900, 500, Color.MAGENTA,null,null);
            addLine(contentPanel,"Product" , 22, true);
            addLine(contentPanel," #ID            Name               status?         price" , 16, true);
            int i = 0;
            for(String[] a : products){
                addButtonedLine(contentPanel, concatenateStrings(a) , 16, true, i++);
            }

            userButton = genButton(1125,10,75,75, 75 , 75 , Color.CYAN, null, "resources/user-icon.jpg");
            adminButton = genButton(1125,130,75,75,75,75,Color.GREEN,null,"resources/admin-icon.jpeg");
            sortButton = genButton(1125,200,75,75,75,75,Color.GRAY,"Display", null);
            cartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
              



                    try {
                                    showCartOption();
                                } catch (SQLException ec) {
                                    // Handle the exception here, or display an error message
                                    ec.printStackTrace();
                                }
                }
            });
            orderButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    try{showOrderOption();}
                    catch(SQLException ea)
                    {
                        ea.printStackTrace();
                    }
                }
            });
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    search();
                }
            });
            userButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        showEditOption();
                    } catch (SQLException ex) {
                        // Handle the exception here
                        ex.printStackTrace();
                    }
                }
            });
            adminButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openUpAdmin();
                }
            });
            sortButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sort();
                }
            });

            mainPanel.add(cartButton);
            mainPanel.add(orderButton);
            mainPanel.add(searchButton);
            mainPanel.add(userButton);
            mainPanel.add(adminButton);
            mainPanel.add(sortButton);
            mainPanel.add(contentPanel);
        }
        private double calculateAverageRating(int productNum) {
            if (ratings.isEmpty()) {
                return 0.0;
            }

            int count = 0;
            int sum = 0;
            for (Integer[] ratingArray : ratings) {
                if (ratingArray[productNum] != null) {
                    sum += ratingArray[productNum];
                    count++;
                }
            }

            if (count == 0) {
                return 0.0;
            }

            return (double) sum / count;
        }
        public void showWindow() {
            setVisible(true);
        }

        public int Order_Number ()throws SQLException
        {
            String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
                try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password)) )
                {
                    Statement stmt =conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT MAX(Order_Number) FROM Shop_Order");
                    if(rs.next())
                    {
                        int a = rs.getInt(1);
                        a++;
                        return a;

                    }


                }catch (Exception e)
                {
                    throw e;
                }


            return 0;
        }

        public void storeOrderInformationinSQL(int Order_Number, String User_ID, String Address) throws SQLException
        {
            String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
                try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password)) )
                {

                    int Address_ID = 0; 
                    PreparedStatement stmt2 = conn.prepareStatement("SELECT Address_ID FROM Address_Book WHERE User_Address = ?");
                    stmt2.setString(1,Address);
                    ResultSet rs2 = stmt2.executeQuery();
                    if(rs2.next())
                    {
                        Address_ID = rs2.getInt(1);
                    }
                    
                    LocalDate nowdate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String modifiedDate = nowdate.format(formatter);

                    PreparedStatement stmt = conn.prepareStatement( "INSERT INTO Shop_Order (Order_Date, Order_Number, User_ID, Address_ID, Payment_ID) VALUES (?, ?, ?, ?, NULL)")  ;    
                    stmt.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now()));
                    stmt.setInt(2, Order_Number);
                    stmt.setInt(3, Integer.parseInt(User_ID));
                    stmt.setInt(4, Address_ID);     
                    stmt.executeUpdate();
                   
                    
                }catch (Exception e)
                {
                    throw e;
                }

        }

        public void UpdateStock (int Order_Number) throws SQLException
        {
            for(int i =0; i < cartAmount.size();i++)
            {
                if(cartAmount.get(i) > 0)
                {  
                    String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
                    try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password)) ) 
                    { 
                        //insert order_details
                        PreparedStatement stmt= conn.prepareStatement(" INSERT INTO Order_Details (Order_Number, Product_ID, Amount, Price) VALUES(?,?,?,?) ");
                        stmt.setInt(1,Order_Number);
                        stmt.setInt(2,  Integer.parseInt ((products.get(i))[0]));
                        stmt.setInt(3,cartAmount.get(i));
                        stmt.setFloat(4,(Float) (cartAmount.get(i) * Float.valueOf(products.get(i)[3]) ));

                        System.out.println("hello" + Integer.parseInt ((products.get(i))[0]));

                        stmt.executeUpdate();


                        PreparedStatement stmt2 = conn.prepareStatement("UPDATE Product_Stock SET Amount = ? WHERE PRODUCT_ID = ?");
                        stmt2.setInt(1, Integer.valueOf(products.get(i)[2]) - cartAmount.get(i));
                        stmt2.setInt(2,Integer.parseInt(products.get(i)[0]));
                        stmt2.executeQuery();
                        products.get(i)[2] = String.valueOf(Integer.parseInt( products.get(i)[2]) - cartAmount.get(i));
                        reDrawPanel();
                    }catch  (SQLException e) {
                    // Handle the exception or throw it to the calling method
                        throw e;
                    }

                }
            }
        }

        private JButton genBP(int x, int y, int width, int height, int imageWidth, int imageHeight, Color color, String text, String imagePath) {
            JButton button = new JButton();
            button.setBounds(x, y, width, height);
            button.setLayout(new BoxLayout(button, BoxLayout.Y_AXIS)); // Use BoxLayout with vertical orientation

            // Create a panel for the image and text
            cartPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            cartPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            if (imagePath != null) {
                ImageIcon imageIcon = new ImageIcon(imagePath);
                // Resize the image to 75x75 pixels
                Image image = imageIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(image);
                cartLabel = new JLabel(resizedIcon);
                cartLabel.setBounds(x, y, 75, 75); // Position the image at (10, 10)
                cartPanel.add(cartLabel);
            }

            // Create the label to display text
            if (text != null) {
                addLine(cartPanel,text,24,false);
            }

            // Set the size of the content panel to match the specified width and height
            cartPanel.setPreferredSize(new Dimension(width, height));
            cartPanel.setMaximumSize(new Dimension(width, height));
            cartPanel.setMinimumSize(new Dimension(width, height));

            cartPanel.setBorder(BorderFactory.createLineBorder(color));
            button.add(cartPanel);
            return button;
        }

        private JButton genButton(int x, int y, int width, int height, int imageWidth, int imageHeight, Color color, String text, String imagePath) {
            JButton button = new JButton();
            button.setBounds(x, y, width, height);
            button.setLayout(new BoxLayout(button, BoxLayout.Y_AXIS)); // Use BoxLayout with vertical orientation

            // Create a panel for the image and text
            JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            if (imagePath != null) {
                ImageIcon imageIcon = new ImageIcon(imagePath);
                // Resize the image to 75x75 pixels
                Image image = imageIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(image);
                JLabel imageLabel = new JLabel(resizedIcon);
                imageLabel.setBounds(x, y, 75, 75); // Position the image at (10, 10)
                contentPanel.add(imageLabel);
            }

            // Create the label to display text
            if (text != null) {
                addLine(contentPanel,text,24,false);
            }

            // Set the size of the content panel to match the specified width and height
            contentPanel.setPreferredSize(new Dimension(width, height));
            contentPanel.setMaximumSize(new Dimension(width, height));
            contentPanel.setMinimumSize(new Dimension(width, height));

            contentPanel.setBorder(BorderFactory.createLineBorder(color));
            button.add(contentPanel);
            return button;
        }
        private String concatenateStrings(String[] strings) {
            StringBuilder sb = new StringBuilder();
            for (String str : strings) {
                sb.append(String.format("%-15s", str));
            }
            return sb.toString();
        }
        private JPanel genPan(int x, int y, int width, int height, Color color,String text, String imagePath) {
            JPanel panel = new JPanel();
            panel.setBounds(x, y, width, height);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Use BoxLayout with vertical orientation

            // Create a panel for the image and text
            JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            if(imagePath != null){
                ImageIcon imageIcon = new ImageIcon(imagePath);
                // Resize the image to 75x75 pixels
                Image image = imageIcon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(image);
                JLabel imageLabel = new JLabel(resizedIcon);
                imageLabel.setBounds(x, y, 75, 75); // Position the image at (10, 10)
                contentPanel.add(imageLabel);
            }
            // Create the label to display text
            if(text != null) {
                addLine(contentPanel,text,24,false);
            }
            panel.setBorder(BorderFactory.createLineBorder(color));
            // Set the size of the panel to match the specified width and height
            panel.setPreferredSize(new Dimension(width, height));
            panel.setMaximumSize(new Dimension(width, height));
            panel.setMinimumSize(new Dimension(width, height));
            if(text != null || imagePath != null){
                panel.add(contentPanel);
            }
            return panel;
        }

        private void addLine(JPanel panel, String text, int font, boolean top){
            JLabel textLabel = new JLabel(text);
            textLabel.setFont(textLabel.getFont().deriveFont(Font.BOLD, font));
            if(top) textLabel.setVerticalAlignment(JLabel.TOP);
            panel.add(textLabel);
        }

        private void addButtonedLine(JPanel panel, String text, int font, boolean top, int productNum) {
            JPanel linePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            linePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel textLabel = new JLabel(text);
            textLabel.setFont(textLabel.getFont().deriveFont(Font.BOLD, font));
            if (top) {
                textLabel.setVerticalAlignment(JLabel.TOP);
            }
            linePanel.add(textLabel);

            JButton addToCartButton = new JButton("Add to Cart");
            addToCartButton.setFont(addToCartButton.getFont().deriveFont(Font.PLAIN, 12)); // Set a smaller font size
            addToCartButton.setPreferredSize(new Dimension(120, 60)); // Set a smaller size for the button

            addToCartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JComboBox<Integer> quantityComboBox = new JComboBox<>();
                    for (int i = 0; i <= 10; i++) {
                        quantityComboBox.addItem(i);
                    }

                    Object[] message = {
                            "Select quantity:", quantityComboBox
                    };

                    int option = JOptionPane.showConfirmDialog(null, message, "Add to Cart", JOptionPane.OK_CANCEL_OPTION);

                    if (option == JOptionPane.OK_OPTION) {
                        int selectedQuantity = (int) quantityComboBox.getSelectedItem();
                        if (selectedQuantity > 0) {
                            cartAmount.set(productNum, cartAmount.get(productNum) + selectedQuantity);
                        }
                    }
                }
            });
            JButton getDetailsButton = new JButton("Details");
            getDetailsButton.setFont(addToCartButton.getFont().deriveFont(Font.PLAIN, 12)); // Set a smaller font size
            getDetailsButton.setPreferredSize(new Dimension(120, 60)); // Set a smaller size for the button

            getDetailsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        String desc = get_info(productNum);
                    JOptionPane.showMessageDialog(null,desc);

                    }catch(SQLException ea)
                    {   ea.printStackTrace();}
                   
                }
            });
            JButton commentButton = new JButton("Comments");
            commentButton.setFont(commentButton.getFont().deriveFont(Font.PLAIN, 12));
            commentButton.setPreferredSize(new Dimension(120, 60));


           commentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            Object[] options = {"See Comments", "Add Comment", "Cancel"};
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Select an option:",
                    "Comment Options",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[2]);

            if (choice == 0) {
                if (comments.isEmpty()) {
                
                    JOptionPane.showMessageDialog(
                            null,
                            "No comments available.",
                            "Comments",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    StringBuilder commentsText = new StringBuilder();
                    for (String[] comment : comments) {
                        commentsText.append(comment[productNum]).append("\n");
                    }

                    JTextArea commentsTextArea = new JTextArea(commentsText.toString());
                    commentsTextArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(commentsTextArea);
                    scrollPane.setPreferredSize(new Dimension(300, 200));

                    double averageRating = calculateAverageRating(productNum);
                    String averageRatingText = String.format("Average Rating: %.2f", averageRating);

                    JPanel panel = new JPanel();
                    panel.setLayout(new BorderLayout());
                    panel.add(scrollPane, BorderLayout.CENTER);
                    panel.add(new JLabel(averageRatingText), BorderLayout.SOUTH);

                    JOptionPane.showMessageDialog(
                            null,
                            panel,
                            "Comments",
                            JOptionPane.PLAIN_MESSAGE
                    );
                }
            } else if (choice == 1) {
                Object[] addOptions = {"Write", "Rating", "Cancel"};
                int addChoice = -1;
                while (addChoice != 2) {
                    addChoice = JOptionPane.showOptionDialog(
                            null,
                            "Select an option:",
                            "Add Comment",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            addOptions,
                            addOptions[2]);

                    if (addChoice == 0) {
                        String comment = JOptionPane.showInputDialog(
                                null,
                                "Enter your comment:",
                                "Write Comment",
                                JOptionPane.PLAIN_MESSAGE
                        );
                        if (comment != null && !comment.isEmpty()) {
                            String[] commentArray = new String[products.size()];
                            if (comments.isEmpty()) {
                                Arrays.fill(commentArray, null);
                            } else {
                                System.arraycopy(comments.get(comments.size() - 1), 0, commentArray, 0, products.size());
                            }
                            commentArray[productNum] = comment;
                            comments.add(commentArray);
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Comment added successfully.",
                                    "Write Comment",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "No comment entered.",
                                    "Write Comment",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    } else if (addChoice == 1) {
                        Integer[] ratingOptions = {1, 2, 3, 4, 5};
                        Integer rating = (Integer) JOptionPane.showInputDialog(
                                null,
                                "Select a rating:",
                                "Rating",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                ratingOptions,
                                ratingOptions[0]
                        );
                        if (rating != null) {
                            Integer[] ratingArray = new Integer[products.size()];
                            if (ratings.isEmpty()) {
                                Arrays.fill(ratingArray, null);
                            } else {
                                System.arraycopy(ratings.get(ratings.size() - 1), 0, ratingArray, 0, products.size());
                            }
                            ratingArray[productNum] = rating;
                            ratings.add(ratingArray);
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Rating added successfully.",
                                    "Rating",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "No rating selected.",
                                    "Rating",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            }
        }
    });

            linePanel.add(commentButton);
            linePanel.add(addToCartButton);
            linePanel.add(getDetailsButton);
            panel.add(linePanel);
        }
        /*
        private double calculateAverageRating(int productNum) {
            if (ratings.isEmpty()) {
                return 0.0;
            }

            int count = 0;
            int sum = 0;
            for (Integer[] ratingArray : ratings) {
                if (ratingArray[productNum] != null) {
                    sum += ratingArray[productNum];
                    count++;
                }
            }

            if (count == 0) {
                return 0.0;
            }

            return (double) sum / count;
        }
            */
        public boolean isStringInArray(String[] array, String target) {
            for (String element : array) {
                if (element.equals(target)) {
                    return true;
                }
            }
            return false;
        }

        private String get_info(int productNum) throws SQLException
        {
             String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
                try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password)) )
                {   
                    String name = products.get(productNum)[1];
                    PreparedStatement stmt1 = conn.prepareStatement("SELECT Product_Descripion FROM Product WHERE Product_Name = ?");
                    stmt1.setString(1,name);
                    ResultSet rs1= stmt1.executeQuery();
                    if(rs1.next())
                    {   return rs1.getString(1);
                        
                    }
                }catch (SQLException ea)
                {
                    throw ea;
                }
                return "";

        }
        private void showCartOption() throws SQLException{
            StringBuilder cartProducts = new StringBuilder();
            cartProducts.append("#ID   Name          status?          price    Amount\n");

            Float totalAmount = 0.0f;

            for (int i = 0; i < products.size(); i++) {
                if (cartAmount.get(i) != 0) {
                    String[] product = products.get(i);
                    cartProducts.append(String.format("%-5s %-10s %-15s %-7s %d\n", product[0], product[1], product[2], product[3], cartAmount.get(i)));
                    totalAmount += Float.parseFloat(product[3].replaceAll("\\s", "")) * cartAmount.get(i);
                }
            }

            cartProducts.append(String.format("\n\n%-60s %s %f\n", "", "Total: $", totalAmount));

            if (cartProducts.length() > 0) {
                int choice = JOptionPane.showOptionDialog(this, cartProducts.toString(), "Cart Products", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"I'll continue shopping", "Buy All", "Clear Cart"}, null);

                if (choice == 1) {
                    if (totalAmount == 0) {
                        JOptionPane.showMessageDialog(this, "No selected cart items. Cannot proceed with payment.", "Payment Error", JOptionPane.ERROR_MESSAGE);
                    }else if(customer.getAddress().equals("To be filled")){
                        JOptionPane.showMessageDialog(this, "No address assigned. Cannot proceed with payment.", "Payment Error", JOptionPane.ERROR_MESSAGE);
                    }

                    //else if(customer.getAddress().equals("To be filled")){
                      //  JOptionPane.showMessageDialog(this, "No address assigned. Cannot proceed with payment.", "Payment Error", JOptionPane.ERROR_MESSAGE);
                    //}
                    
                    else {
                        int paymentChoice = JOptionPane.showOptionDialog(this, "Choose your payment method", "Payment Method", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"PayPal", "PayMe", "CreditCards"}, null);

                        if (paymentChoice == 0) {
                            int orderNumber = Order_Number();
                            JOptionPane.showMessageDialog(this, "Your order has been made successfully using PayPal.\nYour Order number is " + orderNumber, "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
                            // PayPal code
                            // Add your code here for PayPal payment

                            
                            try {
                                   storeOrderInformationinSQL(orderNumber,customer.ID,customer.Address);
                                   UpdateStock(orderNumber);
                                   resetCart();
                                } catch (SQLException e) {
                                    // Handle the exception here, or display an error message
                                    e.printStackTrace();
                                }
                            

                        } else if (paymentChoice == 1) {
                            int orderNumber = Order_Number();
                            JOptionPane.showMessageDialog(this, "Your order has been made successfully using PayMe.\nYour Order number is " + orderNumber, "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
                            // PayMe code
                            // Add your code here for PayMe payment
                             try {
                                   storeOrderInformationinSQL(orderNumber,customer.ID,customer.Address);
                                   UpdateStock(orderNumber);
                                   resetCart();
                                } catch (SQLException e) {
                                    // Handle the exception here, or display an error message
                                    e.printStackTrace();
                                }
                        } else if (paymentChoice == 2) {
                            int orderNumber = Order_Number();
                            JOptionPane.showMessageDialog(this, "Your order has been made successfully using CreditCards.\nYour Order number is " + orderNumber, "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
                            // CreditCards code
                            // Add your code here for CreditCards payment
                             try {
                                   storeOrderInformationinSQL(orderNumber,customer.ID,customer.Address);
                                   UpdateStock(orderNumber);
                                   resetCart();
                                } catch (SQLException e) {
                                    // Handle the exception here, or display an error message
                                    e.printStackTrace();
                                }
                        }
                    }
                }else if (choice == 2){
                    resetCart();
                    JOptionPane.showMessageDialog(this, "No product in the cart", "Cart", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No products in the cart", "Cart", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        private void resetCart() {
            cartAmount = new ArrayList<>(Collections.nCopies(products.size(), 0));
        }
        private String generateWarehouseAddress() {
            Random random = new Random();
            int randomNumber = random.nextInt(900000) + 100000; // Generate a random number between 100000 and 999999
            return "#" + randomNumber;
        }



        private void showOrderOption() throws SQLException{
            StringBuilder orderDetails = new StringBuilder();
            orderDetails.append("Current Order:\n\n");

            Float totalAmount = 0.0f;
            //filling the lastest order details     
            String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
            try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password));) 
            {
                PreparedStatement stmt =conn.prepareStatement("SELECT Max(Order_Number) FROM Shop_Order WHERE User_ID = ?");
                stmt.setInt(1,Integer.parseInt(customer.ID));
                ResultSet rs = stmt.executeQuery();
                if(rs.next())
                {
                    int max_id = rs.getInt(1);
                    PreparedStatement stmt2 = conn.prepareStatement("SELECT Product.Product_ID, Product.Product_Name, Order_Details.Price, Order_Details.Amount FROM Order_Details LEFT OUTER JOIN Product on Order_Details.Product_ID = Product.Product_ID WHERE Order_Details.Order_Number = ? ORDER BY Product.Product_Name ");
                    stmt2.setInt(1, max_id);
                    ResultSet rs2 = stmt2.executeQuery();
                    while(rs2.next())
                    {
                        orderDetails.append(String.format("#ID: %s Name: %s Status: %s\nPrice: %s Amount: %d\n\n",
                            String.valueOf(rs2.getInt(1)), rs2.getString(2), "Avaliable",String.valueOf(rs2.getFloat(3)), rs2.getInt(4)));
                            
                            totalAmount += rs2.getFloat(3);
                    }
                }
            }catch (SQLException e)
            {
                throw e;
            }

            /*
            for (int i = 0; i < products.size(); i++) {
                if (currentOrderAmount.get(i) != 0) {
                    String[] product = products.get(i);
                    orderDetails.append(String.format("#ID: %s Name: %s Status: %s\nPrice: %s Amount: %d\n\n",
                            product[0], product[1], product[2], product[3], currentOrderAmount.get(i)));
                    totalAmount += Integer.parseInt(product[3].replaceAll("\\s", "")) * currentOrderAmount.get(i);
                }
            }
            */
            //cyrrent order address need to be fixed 
            orderDetails.append(String.format("Shipping Address: %s\nTotal Amount: $%f\n", customer.Address, totalAmount));
            //orderDetails.append("Customer address : " + customer.getAddress());
            int choice = JOptionPane.showOptionDialog(this, orderDetails.toString(), "Current Order", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Show Records"}, null);

            if (choice == 0) {

                ArrayList<String> arr = new ArrayList<String>();
                StringBuilder records_more = new StringBuilder();
                records_more.append("Order Records:\n\n");

                int currentRecordIndex = 0; // page
                int numRecords = recordCount; //left right 


                    records_more.append(String.format("Record %d:\n", currentRecordIndex + 1));


                    //records.append(getOrderDetails(currentRecordIndex));
                    
                    try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password)) )
                    {

                        PreparedStatement stmt_more =conn.prepareStatement("SELECT DISTINCT Order_Number FROM Shop_Order WHERE User_ID = ? ORDER BY Order_Number");
                        stmt_more.setInt(1,Integer.parseInt(customer.ID));
                        ResultSet rs_more = stmt_more.executeQuery();
                        while(rs_more.next())
                        {
                            int curr_id = rs_more.getInt(1);
                            PreparedStatement stmt_more_2 = conn.prepareStatement("SELECT Product.Product_ID, Product.Product_Name, Order_Details.Price, Order_Details.Amount FROM Order_Details LEFT OUTER JOIN Product on Order_Details.Product_ID = Product.Product_ID WHERE Order_Details.Order_Number = ? ORDER BY Product.Product_Name ");
                            stmt_more_2.setInt(1, curr_id);
                            ResultSet rs_more_2 = stmt_more_2.executeQuery();
                            while(rs_more_2.next())
                            {
                                records_more.append(String.format("#ID: %s Name: %s Status: %s\nPrice: %s Amount: %d\n\n",
                                String.valueOf(rs_more_2.getInt(1)), rs_more_2.getString(2), "Avaliable",String.valueOf(rs_more_2.getFloat(3)), rs_more_2.getInt(4)));
                               // totalAmount_more += (rs_more_2.getInt(4) * rs_more_2.getFloat(3));
                            }
                            arr.add(records_more.toString());
                            records_more.setLength(0);
                            numRecords = arr.size()-1;

                        }
                        


                    }catch (SQLException e)
                    {
                        throw e; 
                    }
                   




                while (true) {
                    //change the index
                    int navigationChoice = JOptionPane.showOptionDialog(this, arr.get(currentRecordIndex).toString(), "Order Records", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Previous", "Next", "Close"}, null);
                    if (navigationChoice == 0) {
                        if (currentRecordIndex > 0) {
                            currentRecordIndex--;
                        } else {
                            JOptionPane.showMessageDialog(this, "This is the first record.", "Navigation Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (navigationChoice == 1) {
                        if (currentRecordIndex < numRecords - 1) {
                            currentRecordIndex++;
                        } else {
                            JOptionPane.showMessageDialog(this, "This is the last record.", "Navigation Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (navigationChoice == 2) {
                        break;
                    }

                    //records.setLength(0);
                }
            }
        }
        private void reDrawPanel() {
            remove(contentPanel); // Remove the existing contentPanel from the container

            contentPanel = genPan(30, 130, 900, 500, Color.MAGENTA, null, null);
            addLine(contentPanel, "Product", 22, true);
            addLine(contentPanel, " #ID            Name               status?         price", 16, true);
            int i = 0;
            for (String[] a : products) {
                addButtonedLine(contentPanel, concatenateStrings(a), 16, true, i++);
            }

            add(contentPanel); // Add the updated contentPanel back to the container

            revalidate(); // Revalidate the container
            repaint(); // Repaint the container
        }
        private String getOrderDetails(int recordIndex) {
            StringBuilder orderDetails = new StringBuilder();
            int totalAmount = 0;

            for (int i = 0; i < products.size(); i++) {
                if (recordOrderAmount.get(recordIndex).get(i) != 0) {
                    String[] product = products.get(i);
                    orderDetails.append(String.format("#ID: %s ", product[0]));
                    orderDetails.append(String.format("Name: %s ", product[1]));
                    orderDetails.append(String.format("Status: %s\n", product[2]));
                    orderDetails.append(String.format("Price: %s ", product[3]));
                    orderDetails.append(String.format("Amount: %d \n", recordOrderAmount.get(recordIndex).get(i)));
                    totalAmount += Integer.parseInt(product[3].replaceAll("\\s", "")) * recordOrderAmount.get(recordIndex).get(i);
                }
            }

            orderDetails.append(String.format("Order Number: %s\n", recordOrderAddress.get(recordIndex)));
            orderDetails.append(String.format("Total Amount: $%d\n", totalAmount));

            return orderDetails.toString();
        }

        private void storeOrderInformation(String warehouseAddress) {
            currentOrderAddress = warehouseAddress;
            for (int i = 0; i < products.size(); i++) {
                currentOrderAmount.set(i,cartAmount.get(i));
            }
            recordOrderAmount.add(new ArrayList<>(currentOrderAmount));
            recordOrderAddress.add(currentOrderAddress);
            recordCount++;
        }
        private void search() {
            /*
            String sql = "SELECT Product.Product_ID, Product.Product_Name, SUM(Product_Stock.Amount), Product.Price " +
             "FROM Product_Stock " +
             "LEFT OUTER JOIN Product ON Product_Stock.Product_ID = Product.Product_ID " +
             "WHERE Product.Product_Name LIKE ? OR Product.Product_ID LIKE ? OR Product.Price LIKE ?" +
             "GROUP BY Product.Product_Name, Product.Price " +
             "HAVING SUM(Product_Stock.Amount) > 0";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Set the parameters for the placeholders
            String searchParam = "%" + SearchVariable + "%";
            preparedStatement.setString(1, searchParam);
            preparedStatement.setString(2, searchParam);
            preparedStatement.setString(3, searchParam);

            // Execute the query and process the results
            ResultSet resultSet = preparedStatement.executeQuery();
                    
             */
            Object[] searchOptions = {"Keyword","Cancel"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Search by:",
                    "Search Options",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    searchOptions,
                    searchOptions[1]);

            if (choice == 0) {
                ArrayList<String[]> copiedList = new ArrayList<>(products);
                // Search by keyword
                String keyword = JOptionPane.showInputDialog("Enter the keyword:");
                products.clear();
                if (keyword != null && !keyword.isEmpty()) {
                    // Perform search by keyword logic here
                    // Call a method or perform actions to search by keyword
                    for (String[] strings : copiedList) {
                        boolean flag = false;
                        for (String string : strings) {
                            if (string != null && string.trim().contains(keyword)) {
                                flag = true;
                                break; // Exit the inner loop if a match is found
                            }
                        }
                        if (flag) {
                            products.add(strings);
                        }
                    }
                    reDrawPanel();
                }
                products = new ArrayList<>(copiedList);
            } 
        }
        private void showEditOption() throws SQLException {
            Object[] options = {"Edit Information","My Addresses", "Quit"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "UID : " + customer.getID() + "\nName : " + customer.getUserName() + "\nAddress: " + customer.getAddress(), // Include address in the message
                    "Hi " + customer.getUserName() + "!",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (choice == 0) 
            {
                Object[] options2 = {"User Name", "Password", "Add Address", "Cancel"}; // Add "Address" option
                int choice2 = JOptionPane.showOptionDialog(
                        this,
                        "What to edit?",
                        "Editing profile...",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options2, options2[3]);
                String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
                try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password)) )
                {
                    String currName = customer.userName;
                    switch(choice2){
                    //start using sql

                    case 0 :


                        String newName = JOptionPane.showInputDialog("Enter your new name (any):");
                        //set new name
                        PreparedStatement stmt = conn.prepareStatement("UPDATE User_Info SET User_Name = ? WHERE User_Name = ?");
                        stmt.setString(2, currName);
                        stmt.setString(1, newName);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Your name is now " + newName + "!");
                        customer.setUserName((newName));
                        currName = newName;
                        break;
                    case 1 :
                        String pwAttempt1 = JOptionPane.showInputDialog("Enter your new password:");
                        String pwAttempt2 = JOptionPane.showInputDialog("Enter your new password again:");
                        if(pwAttempt1.equals(pwAttempt2)){

                            // edit password
                            PreparedStatement stmt2 = conn.prepareStatement("UPDATE User_Info SET Password = ? WHERE User_Name = ?");
                            stmt2.setString(2, currName);
                            stmt2.setString(1, pwAttempt1);
                            stmt2.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Your password is now " + pwAttempt1 + "!");
                            
                        }
                        else JOptionPane.showMessageDialog(null, "Passwords were different. Edit interupt.");
                        break;
                    case 2:
                        // Add Address
                        String newAddress = JOptionPane.showInputDialog("Enter your new address:");
                        //add new address
                        Statement stmt4 = conn.createStatement() ;
                        ResultSet rs4 = stmt4.executeQuery("SELECT MAX(Address_ID) FROM Address_Book");
                        if(rs4.next())
                        {
                            int a = rs4.getInt(1);
                          
                            
                            a++;
                            
                            PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO Address_Book VALUES(? , ? , ?) ");
                            stmt3.setInt(1, a);
                            stmt3.setString(2, newAddress);
                            stmt3.setInt(3, Integer.valueOf(customer.ID));
                            stmt3.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Address: " + newAddress + " added!");
                            //customer.setAddress(newAddress);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Somethings Wrong, please try again later!");
                        }
                        break;
                }

                } catch (Exception e) {
                throw e;
                }
            }
            else if (choice == 1) {

                String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
                try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password)) )
                {
                    
                    PreparedStatement stmt5 ;
                    stmt5 = conn.prepareStatement("SELECT User_Address FROM Address_Book WHERE User_ID = ? ORDER BY Address_ID ");
                    stmt5.setInt(1, Integer.valueOf(customer.ID));
                    ResultSet rs5 = stmt5.executeQuery();
                    addresses.clear();
                    while(rs5.next())
                    {
                        addresses.add(rs5.getString(1));
                    }

                    Statement test = conn.createStatement();
                    ResultSet rs_test = test.executeQuery("SELECT Order_Details.Product_ID, Product.Product_Name, SUM(Order_Details.Amount) Amount FROM Order_Details LEFT OUTER JOIN Product on Order_Details.Product_ID = Product.Product_ID GROUP BY Order_Details.Product_ID, Product.Product_Name ORDER BY Amount DESC");
                    while(rs_test.next())
                    {

                        System.out.println(String.valueOf(rs_test.getInt(1)) + "  " + rs_test.getString(2)+ "  " + String.valueOf(rs_test.getInt(3)));
                    }
                    
                } catch (Exception e) {
                    throw e;
                }    


                StringBuilder addressesText = new StringBuilder();
                for (String address : addresses) {
                    addressesText.append(address).append("\n");
                }
                Object[] addressOptions = {"Select", "Cancel"};
                int addressChoice = JOptionPane.showOptionDialog(
                        this,
                        addressesText.toString(),
                        "My Addresses",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        addressOptions,
                        addressOptions[1]);
                if (addressChoice == 0) {
                    // Handle address selection
                    Object[] addressButtons = new Object[addresses.size()];
                    for (int i = 0; i < addresses.size(); i++) {
                        addressButtons[i] = addresses.get(i);
                    }

                    String selectedAddress = (String) JOptionPane.showInputDialog(
                            null,
                            "Select your address:",
                            "Address Selection",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            addressButtons,
                            addressButtons[0]);

                    if (selectedAddress != null) {
                        customer.setAddress(selectedAddress);
                        JOptionPane.showMessageDialog(null, "Your address is now " + selectedAddress + "!");
                    }
                }
            }
        }
        private void openUpAdmin() {
            String adminCode = JOptionPane.showInputDialog(this, "Enter admin code:");

            if (adminCode != null && adminCode.equals("ADMIN")) {
                SwingUtilities.invokeLater(() -> {
                    setVisible(false);
                    try{
                    AdminWindow window = new AdminWindow("Admin_OSS", 1200, 700);
                    window.showWindow();
                    }catch (SQLException ea)
                    {
                        ea.printStackTrace();
                    } 
                });
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin code. Access denied.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        private void sort(){
            Object[] sortOptions = {"Ascending", "Descending"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Sort order:",
                    "Sort Options",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    sortOptions,
                    sortOptions[0]);

            if (choice == 0) {
                // Sort the products based on price ascending
                Collections.sort(products, new Comparator<String[]>() {
                    @Override
                    public int compare(String[] o1, String[] o2) {
                        // Assuming the price is stored in the 4th element of each String[]
                        Float price1 = Float.parseFloat(o1[3].trim());
                        Float price2 = Float.parseFloat(o2[3].trim());
                        return Float.compare(price1, price2);
                    }
                });
            } else if (choice == 1) {
                // Sort the products based on price descending
                Collections.sort(products, new Comparator<String[]>() {
                    @Override
                    public int compare(String[] o1, String[] o2) {
                        // Assuming the price is stored in the 4th element of each String[]
                        Float price1 = Float.parseFloat(o1[3].trim());
                        Float price2 = Float.parseFloat(o2[3].trim());
                        return Float.compare(price2, price1);
                    }
                });
            }

            // Update the GUI
            reDrawPanel();
        }
    }

    public static class AdminWindow extends JFrame {
        private ArrayList<String[]> products; // need edit
        private int recordCount = 0;
        private ArrayList<String> recordOrderAddress; //need edit 
        private String currentOrderAddress;
        //private int[][] recordOrderAmount = new int[10][products.length];
        private ArrayList<ArrayList<Integer>> recordOrderAmount;
        private ArrayList<Integer> cartAmount;
        private ArrayList<Integer> currentOrderAmount;
        private ArrayList<String> details;

        private JButton orderButton;
        private JButton searchButton;
        private JPanel contentPanel;
        private JButton userButton;


        private String UID = "aaaadMIN";
        private String name = "ADMIN";
        //cart
        private JButton changeUserButton;
        private JButton reportButton;
        private JButton addButton;

        //need edit (this is initalize)
        //must edit
        public AdminWindow(String title, int width, int height) throws SQLException
        {
            this.products = new ArrayList<>();
            this.recordOrderAmount = new ArrayList<>();
            this.recordOrderAddress = new ArrayList<>();
            this.recordCount = 0;
            //this.details = new ArrayList<>();
                





            String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
            String b_init;
            int a_init,c_init;
            Float d_init;
            try(Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password));)
            {
                int counter_init=  0;
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT Product.Product_ID, Product.Product_Name, SUM(Product_Stock.Amount), Product.Price  FROM Product_Stock LEFT OUTER JOIN Product on Product_Stock.Product_ID = Product.Product_ID GROUP BY Product.Product_ID,Product.Product_Name, Product.Price HAVING SUM(Product_Stock.Amount) > 0 ORDER BY Product.Product_ID");
                while(rs.next() && counter_init< 5)
                {
                    a_init= rs.getInt(1);
                    b_init= rs.getString(2);
                    c_init= rs.getInt(3);
                    d_init= rs.getFloat(4);
                    String arr_init[] = {String.valueOf(a_init),b_init,String.valueOf(c_init),String.valueOf(d_init)};
                    products.add(arr_init);
                    counter_init++;
                }
                Statement stmt2 = conn.createStatement();
                ResultSet rs2 = stmt2.executeQuery("SELECT Product_ID, Product_Comment, Rating FROM Product_Comment");
                /*
                ratings = new ArrayList<ArrayList<Integer>>();
                comments = new ArrayList<ArrayList<String>>();
                for(int i =0 ; i<counter_init;i++)
                    
                    ratings.add(new ArrayList<>());
                    comments.add(new ArrayList<>());
                
                
                while(rs2.next())
                {
                    ratings.get(rs2.getInt(1)).add(rs2.getInt(3));
                    comments.get(rs2.getInt(1)).add(rs2.getString(2));
                }
                */
            }                  
            this.currentOrderAmount = new ArrayList<>(Collections.nCopies(products.size(), 0));
            this.cartAmount = new ArrayList<>(Collections.nCopies(products.size(), 0));
            this.recordOrderAmount = new ArrayList<>();
            this.recordOrderAddress =  new ArrayList<>(10);
            setTitle(title);
            setSize(width, height);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create the main panel with a FlowLayout
            JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            setContentPane(mainPanel);

            // Create the panels and buttonsw
            orderButton = genButton(230, 10, 100,100, 75, 75,Color.BLUE, null, "resources/order-icon.jpeg");
            searchButton = genButton(300, 10, 600, 100, 75, 75,Color.BLACK, "Press here to search for products..." , "resources/search-icon.jpeg");
            contentPanel = genPan(30,130, 900, 500, Color.MAGENTA,null,null);
            addLine(contentPanel,"Product" , 22, true);
            addLine(contentPanel," #ID            Name               status?         price        Quantity" , 16, true);
            int i = 0;
            for(String[] a : products){
                addButtonedLine(contentPanel, concatenateStrings(a) , 16, true, i++);
            }


            userButton = genButton(1125,10,75,75, 75 , 75 , Color.CYAN, null, "resources/admin-icon.jpeg");
            changeUserButton = genButton(1125, 130, 75, 75, 75, 75, Color.GREEN, null, "resources/user-icon.jpg");
            reportButton = genButton(1000, 200, 100, 75, 75, 75, Color.RED, "Report" , null);
            addButton = genButton(1000,300,100,100,75,75,Color.BLUE,"Add_prod" , null);

            orderButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                     showOrderOption();
                                
                }
            });
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    search();
                }
            });
            userButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    try {
                        showEditOption();
                    } catch (SQLException ex) {
                        // Handle the exception here
                        ex.printStackTrace();
                    }
                }
            });
            changeUserButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openUpUser();
                }
            });
            reportButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                try {
                        report();
                    } catch (SQLException ex) {
                        // Handle the exception here
                        ex.printStackTrace();
                    }    
                }
            });
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)  {
                    // Use an input dialog to prompt the admin for ID, Name, Status, Price, and Quantity
                    String input = JOptionPane.showInputDialog(null, "Name, Price, Category and Description. Separated by commas and Categoty must between 1 to 5:");

                    if (input != null && !input.isEmpty()) {
                        String[] values = input.split(",");
                        int a = 0 ;
                        // Check if the input contains all the required fields
                        if (values.length == 4) {
                            try {
                                a = add_prod_to_sql(values); 
                                } catch (SQLException ea) {
                                    // Handle the exception here, or display an error message
                                    ea.printStackTrace();
                                }
                            String[] newRow = { 
                                String.format("%-5s", String.valueOf(a)),
                                String.format("%-10s", values[0].trim()),
                                String.format("%-15s", "Available"),
                                String.format("%-7s", values[1].trim()),
                                String.format("%-8s", "20")
                            };

                            // Add the new row to the products list
                            products.add(newRow);
                            

                            

                            // Update the GUI or perform any other necessary actions
                            reDrawPanel();
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid input! Please provide ID, Name, Status, Price, and Quantity.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Cannot add with null input!!");
                    }
                }
            });
            

            mainPanel.add(orderButton);
            mainPanel.add(searchButton);
            mainPanel.add(userButton);
            mainPanel.add(changeUserButton);
            mainPanel.add(contentPanel);
            mainPanel.add(reportButton);
            mainPanel.add(addButton);
            mainPanel.setBackground(Color.GREEN);
        }

        private int add_prod_to_sql (String[] arr) throws SQLException
            {
                String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
                int a =0 ;
                try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password)) )
                {   
                    
                    Statement stmt1 = conn.createStatement();
                    ResultSet rs1 = stmt1.executeQuery("SELECT Max(Product_ID) FROM Product");
                    if(rs1.next())
                    {   
                        System.out.println(rs1.getInt(1));
                        a= (rs1.getInt(1));
                        a++;
                        PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO Product (Product_ID, Product_Name, Product_Descripion, Price, Category_ID) VALUES (?, ?, ?, ?, ?)");
                        stmt2.setInt(1, a);
                        stmt2.setString(2, arr[0]);
                        stmt2.setString(3,arr[3]);
                        stmt2.setFloat(4, Float.parseFloat(arr[1]));
                        stmt2.setInt(5, Integer.parseInt(arr[2]));
                        stmt2.executeQuery();
                    }
                    

                }catch (SQLException e)
                {
                    throw e;
                }
                
                return a; // where a is the product number
            }
        public void showWindow() {
            setVisible(true);
        }


        private JButton genButton(int x, int y, int width, int height, int imageWidth, int imageHeight, Color color, String text, String imagePath) {
            JButton button = new JButton();
            button.setBounds(x, y, width, height);
            button.setLayout(new BoxLayout(button, BoxLayout.Y_AXIS)); // Use BoxLayout with vertical orientation

            // Create a panel for the image and text
            JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            if (imagePath != null) {
                ImageIcon imageIcon = new ImageIcon(imagePath);
                // Resize the image to 75x75 pixels
                Image image = imageIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(image);
                JLabel imageLabel = new JLabel(resizedIcon);
                imageLabel.setBounds(x, y, 75, 75); // Position the image at (10, 10)
                contentPanel.add(imageLabel);
            }

            // Create the label to display text
            if (text != null) {
                addLine(contentPanel, text, 24, false);
            }

            // Set the size of the content panel to match the specified width and height
            contentPanel.setPreferredSize(new Dimension(width, height));
            contentPanel.setMaximumSize(new Dimension(width, height));
            contentPanel.setMinimumSize(new Dimension(width, height));

            contentPanel.setBorder(BorderFactory.createLineBorder(color));
            button.add(contentPanel);
            return button;
        }

        private String concatenateStrings(String[] strings) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 2; i++) {
                sb.append(String.format("%-15s", strings[i]));
            }
            return sb.toString();
        }

        private JPanel genPan(int x, int y, int width, int height, Color color, String text, String imagePath) {
            JPanel panel = new JPanel();
            panel.setBounds(x, y, width, height);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Use BoxLayout with vertical orientation

            // Create a panel for the image and text
            JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            if (imagePath != null) {
                ImageIcon imageIcon = new ImageIcon(imagePath);
                // Resize the image to 75x75 pixels
                Image image = imageIcon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(image);
                JLabel imageLabel = new JLabel(resizedIcon);
                imageLabel.setBounds(x, y, 75, 75); // Position the image at (10, 10)
                contentPanel.add(imageLabel);
            }
            // Create the label to display text
            if (text != null) {
                addLine(contentPanel, text, 24, false);
            }
            panel.setBorder(BorderFactory.createLineBorder(color));
            // Set the size of the panel to match the specified width and height
            panel.setPreferredSize(new Dimension(width, height));
            panel.setMaximumSize(new Dimension(width, height));
            panel.setMinimumSize(new Dimension(width, height));
            if (text != null || imagePath != null) {
                panel.add(contentPanel);
            }
            return panel;
        }

        private void addLine(JPanel panel, String text, int font, boolean top) {
            JLabel textLabel = new JLabel(text);
            textLabel.setFont(textLabel.getFont().deriveFont(Font.BOLD, font));
            if (top) textLabel.setVerticalAlignment(JLabel.TOP);
            panel.add(textLabel);
        }

        private void addButtonedLine(JPanel panel, String text, int font, boolean top, int productNum) {
            JPanel linePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            linePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel textLabel = new JLabel(text);
            textLabel.setFont(textLabel.getFont().deriveFont(Font.BOLD, font));
            if (top) {
                textLabel.setVerticalAlignment(JLabel.TOP);
            }
            linePanel.add(textLabel);

            JButton getDetailsButton = new JButton("Details");
            getDetailsButton.setFont(getDetailsButton.getFont().deriveFont(Font.PLAIN, 12)); // Set a smaller font size
            getDetailsButton.setPreferredSize(new Dimension(120, 60)); // Set a smaller size for the button

            getDetailsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //implement
                    JOptionPane.showMessageDialog(null,details.get(productNum));
                }
            });

            JButton editButton = new JButton("Edit");
            editButton.setFont(getDetailsButton.getFont().deriveFont(Font.PLAIN, 12)); // Set a smaller font size
            editButton.setPreferredSize(new Dimension(120, 60)); // Set a smaller size for the button

            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] options = {"Edit Name","Edit Price" ,"Edit Description" , "Edit Inventory", "Delete Item"};
                    int choice = JOptionPane.showOptionDialog(null, "Choose an option:", "Edit Product", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                    if (choice == 0) {
                        // Edit Name
                        String newName = JOptionPane.showInputDialog(null, "Enter the new name:");
                        // Update the name in the products list
                        if(newName != null && !newName.equals("")) {
                            try {
                                edit_product_name(productNum, newName); // no. in product arr;
                        } catch (SQLException ea) {
                            // Handle the exception here, or display an error message
                            ea.printStackTrace();
                        }

                            products.get(productNum)[1] = String.format("%-10s", newName);
                            // Update the GUI or perform any other necessary actions
                            reDrawPanel();
                        }    else{
                            JOptionPane.showMessageDialog(null, "Cannot edit with null input!!");
                        }
                    } else if (choice == 1) {
                        // Edit Price
                        String newPrice = JOptionPane.showInputDialog(null, "Enter the new price:");
                        // Update the price in the products list
                        if(newPrice != null && !newPrice.equals("")) {
                            try {
                                edit_product_Price(productNum, newPrice); // no. in product arr;
                            } catch (SQLException ea) {
                            // Handle the exception here, or display an error message
                            ea.printStackTrace();
                            }    

                            products.get(productNum)[3] = String.format("%-7s", newPrice);
                            // Update the GUI or perform any other necessary actions
                            reDrawPanel();
                        }   else{
                            JOptionPane.showMessageDialog(null, "Cannot edit with null input!!");
                        }
                    } else if (choice == 2) {
                        // Edit Description
                        String newDescription = JOptionPane.showInputDialog(null, "Enter the new description:");
                        // Update the Description in the products list
                        if(newDescription != null && !newDescription.equals("")) {
                            try {
                                edit_product_Description(productNum, newDescription); // no. in product arr;
                            } catch (SQLException ea) {
                            // Handle the exception here, or display an error message
                            ea.printStackTrace();
                            }
                            //details.set(productNum,newDescription)  ;
                            // Update the GUI or perform any other necessary actions
                            reDrawPanel();
                        }   else{
                            JOptionPane.showMessageDialog(null, "Cannot edit with null input!!");
                        }
                    } else if (choice == 3) {
                        // Edit Inventory
                        String quantityInput = JOptionPane.showInputDialog(null, "Enter the new inventory quantity:");
                        // Update the Inventory in the cartAmount list
                        if (quantityInput != null && !quantityInput.isEmpty()) {
                            try {
                                int newQuantity = Integer.parseInt(quantityInput);
                                if (newQuantity < 0) {
                                    JOptionPane.showMessageDialog(null, "Invalid quantity! Quantity should be a non-negative number.");
                                } else {
                                    try {
                                        edit_product_Inventory(productNum, quantityInput);
                                    } catch (SQLException ea) 
                                    {
                                        // Handle the exception here, or display an error message
                                        ea.printStackTrace();
                                    }
                                    products.get(productNum)[4] = String.format("%-8s",newQuantity) ;
                                    // Update the GUI or perform any other necessary actions
                                    reDrawPanel();
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null, "Invalid input! Quantity should be a valid number.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Cannot edit with null input!!");
                        }

                    }
                    else if (choice == 4)
                    {
                        
                        // Update the price in the products list
                        
                            try {
                                edit_product_Price_del(productNum, "0"); // no. in product arr;
                            } catch (SQLException ea) {
                            // Handle the exception here, or display an error message
                            ea.printStackTrace();
                            }    

                            products.get(productNum)[3] = String.format("%-7s", "0");
                            // Update the GUI or perform any other necessary actions
                            reDrawPanel();
                           
                    }
                }
            });


            linePanel.add(getDetailsButton);
            linePanel.add(editButton);
            panel.add(linePanel);
        }

        private void edit_product_name(int productNum, String newName) throws SQLException
        {
                String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
                try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password)) )
                {   
                    String name = products.get(productNum)[1];
                    PreparedStatement stmt1 = conn.prepareStatement("SELECT Product_ID FROM Product WHERE Product_Name = ?");
                    stmt1.setString(1,name);
                    ResultSet rs1= stmt1.executeQuery();
                    if(rs1.next())
                    {   int product_id = rs1.getInt(1);
                        PreparedStatement stmt  = conn.prepareStatement("UPDATE Product SET Product_Name = ? WHERE Product_ID = ?");
                        stmt.setString(1,newName);
                        stmt.setInt(2,product_id);
                        stmt.executeUpdate();
                    }
                }catch (SQLException ea)
                {
                    throw ea;
                }
        }
        private void edit_product_Price(int productNum, String price) throws SQLException
        {
            String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
            try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password)) )
            {   
                String name = products.get(productNum)[1];
                PreparedStatement stmt1 = conn.prepareStatement("SELECT Product_ID FROM Product WHERE Product_Name = ?");
                stmt1.setString(1,name);
                ResultSet rs1= stmt1.executeQuery();
                if(rs1.next())
                {   int product_id = rs1.getInt(1);
                    System.out.println(Float.parseFloat(price));
                    PreparedStatement stmt  = conn.prepareStatement("UPDATE Product SET Price = ? WHERE Product_ID = ?");
                    stmt.setFloat(1,Float.parseFloat(price));
                    stmt.setInt(2,product_id);
                    stmt.executeUpdate();
                }
            }catch (SQLException ea)
            {
                throw ea;
            }
        }
        private void edit_product_Price_del(int productNum, String price) throws SQLException
        {
            String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
            try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password)) )
            {   
                String name = products.get(productNum)[1];
                PreparedStatement stmt1 = conn.prepareStatement("SELECT Product_ID FROM Product WHERE Product_Name = ?");
                stmt1.setString(1,name);
                ResultSet rs1= stmt1.executeQuery();
                if(rs1.next())
                {   int product_id = rs1.getInt(1);
                    System.out.println(Float.parseFloat(price));
                    PreparedStatement stmt  = conn.prepareStatement("UPDATE Product SET Price = ? WHERE Product_ID = ?");
                    Float del = -1.0f;
                    stmt.setFloat(1,del);
                    stmt.setInt(2,product_id);
                    stmt.executeUpdate();
                }
            }catch (SQLException ea)
            {
                throw ea;
            }
        }
        private void edit_product_Description(int productNum, String newDescription) throws SQLException
        {
            String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
            try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password)) )
            {   
                String name = products.get(productNum)[1];
                PreparedStatement stmt1 = conn.prepareStatement("SELECT Product_ID FROM Product WHERE Product_Name = ?");
                stmt1.setString(1,name);
                ResultSet rs1= stmt1.executeQuery();
                if(rs1.next())
                {   int product_id = rs1.getInt(1);
                    PreparedStatement stmt  = conn.prepareStatement("UPDATE Product SET Product_Descripion = ? WHERE Product_ID = ?");
                    stmt.setString(1,newDescription);
                    stmt.setInt(2,product_id);
                    stmt.executeUpdate();
                }
            }catch (SQLException ea)
            {
                throw ea;
            }
        }
        private void edit_product_Inventory(int productNum, String quantityInput) throws SQLException
        {
        String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
            try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password)) )
            {   
                String name = products.get(productNum)[1];
                PreparedStatement stmt1 = conn.prepareStatement("SELECT Product_ID FROM Product WHERE Product_Name = ?");
                stmt1.setString(1,name);
                ResultSet rs1= stmt1.executeQuery();    
                if(rs1.next())
                {   int product_id = rs1.getInt(1);
                    PreparedStatement stmt = conn.prepareStatement("UPDATE Product_Stock SET Amount = ? WHERE Product_ID = ?");
                   
                    stmt.setInt(2,product_id);
                    stmt.setInt(1,Integer.parseInt(quantityInput));
                    System.out.println(product_id);
                    stmt.executeUpdate();
                }
            }catch (SQLException ea)
            {
                throw ea;
            }
        }


        //update the window
        //after update the product array (searching)
        private void reDrawPanel() {
            remove(contentPanel); // Remove the existing contentPanel from the container

            contentPanel = genPan(30, 130, 900, 500, Color.MAGENTA, null, null);
            addLine(contentPanel, "Product", 22, true);
            addLine(contentPanel, " #ID            Name               status?         price", 16, true);
            int i = 0;
            for (String[] a : products) {
                addButtonedLine(contentPanel, concatenateStrings(a), 16, true, i++);
            }

            add(contentPanel); // Add the updated contentPanel back to the container

            revalidate(); // Revalidate the container
            repaint(); // Repaint the container
        }

        public boolean isStringInArray(String[] array, String target) {
            for (String element : array) {
                if (element.equals(target)) {
                    return true;
                }
            }
            return false;
        }


        //detail button
        private void showOrderOption() {
            StringBuilder orderDetails = new StringBuilder();
            orderDetails.append("Current Order:\n\n");

            int totalAmount = 0;

            for (int i = 0; i < products.size(); i++) {
                if (currentOrderAmount.get(i) != 0) {
                    String[] product = products.get(i);
                    orderDetails.append(String.format("#ID: %s Name: %s Status: %s\nPrice: %s Amount: %d\n\n",
                            product[0], product[1], product[2], product[3], currentOrderAmount.get(i)));
                    totalAmount += Integer.parseInt(product[3].replaceAll("\\s", "")) * currentOrderAmount.get(i);
                }
            }

            orderDetails.append(String.format("Warehouse Address: %s\n", currentOrderAddress));
            orderDetails.append(String.format("Total Amount: $%d\n", totalAmount));

            int choice = JOptionPane.showOptionDialog(this, orderDetails.toString(), "Current Order", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Show Records"}, null);

            if (choice == 0) {
                StringBuilder records = new StringBuilder();
                records.append("Order Records:\n\n");

                int currentRecordIndex = 0;
                int numRecords = recordCount;

                while (true) {
                    records.append(String.format("Record %d:\n", currentRecordIndex + 1));
                    records.append(getOrderDetails(currentRecordIndex));
                    records.append("\n");

                    int navigationChoice = JOptionPane.showOptionDialog(this, records.toString(), "Order Records", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Previous", "Next", "Close"}, null);

                    if (navigationChoice == 0) {
                        if (currentRecordIndex > 0) {
                            currentRecordIndex--;
                        } else {
                            JOptionPane.showMessageDialog(this, "This is the first record.", "Navigation Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (navigationChoice == 1) {
                        if (currentRecordIndex < numRecords - 1) {
                            currentRecordIndex++;
                        } else {
                            JOptionPane.showMessageDialog(this, "This is the last record.", "Navigation Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (navigationChoice == 2) {
                        break;
                    }

                    records.setLength(0);
                }
            }
        }
        //get what the user buy from the OSS
        //show record
        private String getOrderDetails(int recordIndex) {
            StringBuilder orderDetails = new StringBuilder();
            int totalAmount = 0;

            for (int i = 0; i < products.size(); i++) {
                if (recordOrderAmount.get(recordIndex).get(i) != 0) {
                    String[] product = products.get(i);
                    orderDetails.append(String.format("#ID: %s ", product[0]));
                    orderDetails.append(String.format("Name: %s ", product[1]));
                    orderDetails.append(String.format("Status: %s\n", product[2]));
                    orderDetails.append(String.format("Price: %s ", product[3]));
                    orderDetails.append(String.format("Amount: %d \n", recordOrderAmount.get(recordIndex).get(i)));
                    totalAmount += Integer.parseInt(product[3].replaceAll("\\s", "")) * recordOrderAmount.get(recordIndex).get(i);
                }
            }

            orderDetails.append(String.format("Order Number: %s\n", recordOrderAmount.get(recordIndex)));
            orderDetails.append(String.format("Total Amount: $%d\n", totalAmount));

            return orderDetails.toString();
        }
        //real searching
        //edit the product arr
        private void search() {

        }


        //User and admin edit personal data 
        private void showEditOption() throws SQLException {
            Object[] options = {"Edit?", "Okay cool"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "UID : " + UID + "\nName : " + name,
                    "Hi " + name + "!",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[1]);
            if (choice == 0) {
                Object[] options2 = {"UID", "Name", "Password", "Cancel"};
                int choice2 = (JOptionPane.showOptionDialog(
                        this,
                        "What to edit?",
                        "Editting profile...",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options2, options2[3]));
                switch (choice2) {
                    case 0:
                        String newUID = JOptionPane.showInputDialog("Enter your new UID (8-digit):");
                        if (newUID.length() == 8) {
                            UID = newUID;
                            JOptionPane.showMessageDialog(null, "Your UID is now " + newUID + "!");
                        } else JOptionPane.showMessageDialog(null, "UID was not in 8 characters");
                        break;
                    case 1:
                        String newName = JOptionPane.showInputDialog("Enter your new name (any):");
                        JOptionPane.showMessageDialog(null, "Your name is now " + newName + "!");
                        name = newName;
                        break;
                    case 2:
                        String pwAttempt1 = JOptionPane.showInputDialog("Enter your new password:");
                        String pwAttempt2 = JOptionPane.showInputDialog("Enter your new password again:");
                        if (pwAttempt1.equals(pwAttempt2))
                            JOptionPane.showMessageDialog(null, "Your password is now " + pwAttempt1 + "!");
                        else JOptionPane.showMessageDialog(null, "Passwords were different.");
                        break;
                }
            }
        }

        private static void report() throws SQLException {
            System.out.print("123");
            Object[] reportOptions = {"Sales Rank", "Favorite Product", "Favorite Category", "Cancel"};
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Generate report:",
                    "Report Options",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    reportOptions,
                    reportOptions[3]);
            String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
            ArrayList<String> arr = new ArrayList<>();

            if (choice == 0) {
                

                try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password))) {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT Order_Details.Product_ID, Product.Product_Name, SUM(Order_Details.Amount) Amount FROM Order_Details LEFT OUTER JOIN Product on Order_Details.Product_ID = Product.Product_ID GROUP BY Order_Details.Product_ID, Product.Product_Name ORDER BY Amount DESC");

                    while (rs.next()) {
                        arr.add(rs.getString(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
                    }

                    // Create a formatted string with all elements of arr
                    
                    StringBuilder message = new StringBuilder();
                    message.append("Product ID       Product Name         Amount\n");
                    for (int i = 0; i < arr.size(); i++) {
                        message.append(arr.get(i)).append("\n");
                    }

                    // Show the contents of arr in a JOptionPane
                    JOptionPane.showMessageDialog(null, message.toString(), "Results", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (choice == 1) {


                try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password))) {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT Product.Product_Name, AVG(Product_Comment.Rating) Rating FROM Product_Comment, Product WHERE Product_Comment.Product_ID = Product.Product_ID GROUP BY Product.Product_Name ORDER BY Rating DESC");

                    while (rs.next()) {
                        
                        arr.add(rs.getString(1) + "  " + rs.getFloat(2) );
                    }

                    // Create a formatted string with all elements of arr
                    StringBuilder message = new StringBuilder();
                    message.append("Product Name      Rating\n");
                    for (int i = 0; i < arr.size(); i++) {
                        message.append(arr.get(i)).append("\n");
                    }

                    // Show the contents of arr in a JOptionPane
                    JOptionPane.showMessageDialog(null, message.toString(), "Results", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                // Generate favorite product report
                // Call a method or perform actions to generate the favorite product report
            } else if (choice == 2) {
                try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password))) {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT Product.Category_ID, Category.Category_Name, SUM(Order_Details.Amount) Amount FROM (Order_Details LEFT OUTER JOIN Product on Order_Details.Product_ID = Product.Product_ID) LEFT OUTER JOIN Category on Product.Category_ID = Category.Category_ID GROUP BY Product.Category_ID, Category_Name ORDER BY Amount DESC" );

                    while (rs.next()) {
                        arr.add(rs.getString(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
                    }

                    // Create a formatted string with all elements of arr
                    StringBuilder message = new StringBuilder();
                    message.append("Category ID       Category Name         Amount\n");
                    for (int i = 0; i < arr.size(); i++) {
                        message.append(arr.get(i)).append("\n");
                    }

                    // Show the contents of arr in a JOptionPane
                    JOptionPane.showMessageDialog(null, message.toString(), "Results", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                
                // Generate favorite category report
                // Call a method or perform actions to generate the favorite category report
            }
        }
       
        //admin -> user's view
        private void openUpUser() {
            String yes = JOptionPane.showInputDialog(this, "Change to user? (Y/N)");

            if (yes.equals("Y")) {
                SwingUtilities.invokeLater(() -> {
                    setVisible(false);
                    try {
                        CustomerWindow window = new CustomerWindow("OSS", 1200, 700, new Customer("dummy", "123", "1238642", "To be filled in"));
                        window.showWindow();
                    } catch (SQLException e) {
                        // Handle the exception here, or display an error message
                        e.printStackTrace();
                    }
                });
            } else {
                JOptionPane.showMessageDialog(this, "Proceed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private static void performDatabaseOperation() throws SQLException {
        // Replace with your actual database connection details
        String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";

        // Connect to the database
        try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password));
        ) {
//            conn.close();

        } catch (SQLException e) {
            // Handle the exception or throw it to the calling method
            throw e;
        }
    }
    public static void main(String[] args) throws SQLException {
        //oracle connection
        Console console = System.console();
        System.out.print("Enter your username: ");
        oracle_user = console.readLine();
        System.out.print("Enter your password: ");
        oracle_password = console.readPassword();

        String pwd = String.valueOf(oracle_password);
        performDatabaseOperation();

        // initialize
        boolean flag2 = true;
        while(flag2) {
            String[] options = {"Yes", "No"};
            int choice2 = JOptionPane.showOptionDialog(null, "initialize or not:", "initialize or not",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if(choice2 == 0) {
                flag2 = false;
                oracle_initialize();
            }
            else if (choice2 == 1) {
                flag2 = false;
            }
            else{
                flag2 = false;
            }
        }

        //loadCustomersFromFile();

        boolean flag = true;
        while(flag) {
            String[] options = {"Login", "Register"};
            int choice = JOptionPane.showOptionDialog(null, "Select an option:", "Login or Register",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (choice == 0) {
                flag = login();
            } else if (choice == 1) {
                register();
            }else{
                flag = false;
            }
        }
    }
        

    private static boolean login() throws SQLException {
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(Box.createHorizontalStrut(15)); // Add spacing
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showOptionDialog(null, panel, "Login",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (result == JOptionPane.OK_OPTION)
        {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            //login time

            String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";

            // Connect to the database
            try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password)) ) 
            {
                PreparedStatement stmt;
                stmt =  conn.prepareStatement("SELECT User_Name FROM User_info WHERE User_Name = ?");
                stmt.setString(1, username);
                //stmt.setString(1, username);
                
                ResultSet rs =  stmt.executeQuery();

                if(rs.next())
                {
                        PreparedStatement stmt4;
                        stmt4 = conn.prepareStatement("SELECT Password FROM User_info WHERE User_Name Like ?");
                        stmt4.setString(1, username+ "%");
                        ResultSet rs3 =  stmt4.executeQuery();
                        rs3.next();
                        
                        

                        if(Objects.equals(rs3.getString(1), password))
                        {
                            int type = 0;
                            String a ,b,c,d;
                            JOptionPane.showMessageDialog(null, "Login successful!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
                            // Do further process or display the main application window
                            try { 
                            PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM User_info WHERE User_Name = ?");
                            stmt2.setString(1, username);
                            ResultSet rs2 =  stmt2.executeQuery();

                            rs2.next();
                            String type2 = rs2.getString("User_Type");
                            System.out.println(type2);
                            if(type2.trim().equals("Customer"))
                            {
                                type= 0;
                            }
                            else
                            {
                                type=1;
                            } System.out.println(type);
                            Customer customer = new Customer(rs2.getString("User_Name"),rs2.getString("Password"),String.valueOf(rs2.getInt("User_ID")),"To be filled in");
                            PreparedStatement hello = conn.prepareStatement("SELECT User_Address FROM Address_Book WHERE User_ID = ?");
                            hello.setInt(1, rs2.getInt("User_ID"));
                            ResultSet rs_hello = hello.executeQuery();
                            if(rs_hello.next() == true){
                                d = rs_hello.getString("User_Address");   
                            }
                            else{
                                d = "To be filled in ";
                            }
                            a = customer.userName; b = customer.password; c= customer.ID; 
                            } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            if(type == 0)
                            {
                                SwingUtilities.invokeLater(() -> {
                                    try {
                                        CustomerWindow customerWindow = new CustomerWindow("OSS", 1200, 700, new Customer(a, b, c, d));
                                        customerWindow.showWindow();
                                    } catch (SQLException e) {
                                        // Handle the exception here, or display an error message
                                        e.printStackTrace();
                                    }
                                });
                            }
                            else
                            {
                                SwingUtilities.invokeLater(() -> {
                                try {
                                    AdminWindow window = new AdminWindow("Admin_OSS", 1200, 700);
                                    window.showWindow();
                                } catch (SQLException e) {
                                    // Handle the exception here, or display an error message
                                    e.printStackTrace();
                                }
                            });
                            }

                            return false;
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Login failed. Invalid username or password.",
                                    "Login Failed", JOptionPane.ERROR_MESSAGE);
                            return true;
                        }
                    
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Login failed. Invalid username or password.",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                    return true;
                }


            } catch (SQLException e) {
                // Handle the exception or throw it to the calling method
                throw e;
            }

        }
        return true;
    }

    private static void register() throws SQLException {
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(Box.createHorizontalStrut(15)); // Add spacing
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showOptionDialog(null, panel, "Register",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (result == JOptionPane.OK_OPTION)
        {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Check if the username is already taken
            String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
            try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password)) )
            {
                PreparedStatement stmt = conn.prepareStatement("SELECT count(*) FROM User_info WHERE User_Name != ?");
                stmt.setString(1, username);
                ResultSet rs =  stmt.executeQuery();
                Statement ss = conn.createStatement();
                ResultSet rss = ss.executeQuery("SELECT count(*) FROM User_info");
                rs.next(); rss.next();
                System.out.println("abc");
                int num1 = rs.getInt(1), num2 = rss.getInt(1);



                System.out.println("");
                if(num1 != num2)
                {
                    //hv ppl use this name
                    JOptionPane.showMessageDialog(null, "Username is already taken. Please choose a different username.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    //create again\
                    System.out.println("");
                    PreparedStatement stmt3 = conn.prepareStatement("SELECT MAX(User_ID) FROM User_info");
                    ResultSet rs2 = stmt3.executeQuery();

                    if(rs2.next())
                    {
                        int a  = rs2.getInt(1);
                        a++;

                    PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO User_info (User_ID, User_Name, User_Type, Verified, Password) VALUES (?, ?, 'Customer', 0, ?)")  ;
                    stmt2.setInt(1,a);
                    stmt2.setString(2,username);
                    stmt2.setString(3,password);
                    stmt2.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Registration successful! Your ID is: " + String.valueOf(a),
                        "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                }


            } catch(SQLException e)
            {
                throw e;
            }
        }
    
    }
    private static void oracle_initialize() throws SQLException {
        String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";

        // Connect to the database
        try (Connection conn = DriverManager.getConnection(url, oracle_user, String.valueOf(oracle_password));) {
            Statement stmt = conn.createStatement();
            stmt.executeQuery("CREATE TABLE helloworld(abcid NUMBER)");
            stmt.executeUpdate("INSERT INTO helloworld VALUES(1)");
            stmt.executeUpdate("INSERT INTO helloworld VALUES(2)");
            conn.close();

        } catch (SQLException e) {
            // Handle the exception or throw it to the calling method
            throw e;
        }
    }
}