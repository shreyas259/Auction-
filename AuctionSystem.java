import java.awt.*;
import javax.swing.*;

class User {
    String name;

    User(String name) {
        this.name = name;
    }
}

class Bid {
    User bidder;
    double amount;

    Bid(User bidder, double amount) {
        this.bidder = bidder;
        this.amount = amount;
    }
}

class AuctionItem {
    String itemName;
    double basePrice;
    Bid highestBid;

    AuctionItem(String itemName, double basePrice) {
        this.itemName = itemName;
        this.basePrice = basePrice;
        this.highestBid = null;
    }

    boolean placeBid(User user, double amount) {
        if (amount < basePrice) return false;
        if (highestBid == null || amount > highestBid.amount) {
            highestBid = new Bid(user, amount);
            return true;
        }
        return false;
    }

    String getStatus() {
        if (highestBid == null) {
            return "<html><b>No bids yet.</b><br>Base Price: $" + basePrice + "</html>";
        } else {
            return "<html><b>Highest Bid:</b> $" + highestBid.amount +
                    " by " + highestBid.bidder.name + "</html>";
        }
    }

    String getWinner() {
        if (highestBid == null) {
            return "No bids placed. Item not sold.";
        } else {
            return "Winner: " + highestBid.bidder.name + " with $" + highestBid.amount;
        }
    }
}

public class AuctionSystem {
    private static AuctionItem auctionItem;
    private static JLabel statusLabel;
    private static JTextField nameField;
    private static JTextField bidField;
    private static JButton bidButton, endButton;

    public static void main(String[] args) {
        // Host enters item details
        JPanel hostPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField itemField = new JTextField();
        JTextField priceField = new JTextField();
        JButton startButton = new JButton("Start Auction");

        hostPanel.add(new JLabel("Host Name:"));
        hostPanel.add(new JTextField()); // Not used in logic, just for UI
        hostPanel.add(new JLabel("Item Name:"));
        hostPanel.add(itemField);
        hostPanel.add(new JLabel("Base Price:"));
        hostPanel.add(priceField);

        int result = JOptionPane.showConfirmDialog(null, hostPanel, "Host Auction", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        String itemName = itemField.getText().trim();
        String basePriceStr = priceField.getText().trim();

        if (itemName.isEmpty() || basePriceStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter item name and base price.");
            return;
        }

        double basePrice;
        try {
            basePrice = Double.parseDouble(basePriceStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid base price.");
            return;
        }

        // Set up auction with host's choices
        auctionItem = new AuctionItem(itemName, basePrice);

        // Main Frame
        JFrame frame = new JFrame("Online Auction System - " + itemName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Top panel for status
        statusLabel = new JLabel(auctionItem.getStatus(), SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.add(statusLabel, BorderLayout.NORTH);

        // Center form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        nameField = new JTextField();
        bidField = new JTextField();

        formPanel.add(new JLabel("Your Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Your Bid Amount:"));
        formPanel.add(bidField);
        frame.add(formPanel, BorderLayout.CENTER);

        // Bottom panel for buttons
        JPanel buttonPanel = new JPanel();
        bidButton = new JButton("Place Bid");
        endButton = new JButton("End Auction");

        buttonPanel.add(bidButton);
        buttonPanel.add(endButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        bidButton.addActionListener(e -> placeBid());
        endButton.addActionListener(e -> endAuction(frame));

        // Show frame
        frame.setVisible(true);
    }

    private static void placeBid() {
        String name = nameField.getText().trim();
        String bidStr = bidField.getText().trim();

        if (name.isEmpty() || bidStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter name and bid amount.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(bidStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid bid amount.");
            return;
        }

        User user = new User(name);
        boolean success = auctionItem.placeBid(user, amount);

        if (success) {
            JOptionPane.showMessageDialog(null, "✅ Bid placed successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "❌ Bid too low or not higher than current bid.");
        }

        statusLabel.setText(auctionItem.getStatus());
        nameField.setText("");
        bidField.setText("");
    }

    private static void endAuction(JFrame frame) {
        JOptionPane.showMessageDialog(frame, "🏁 " + auctionItem.getWinner());
        bidButton.setEnabled(false);
        endButton.setEnabled(false);
    }
}
