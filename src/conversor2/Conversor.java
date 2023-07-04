package conversor2;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Conversor extends JFrame {

    private JPanel contentPane;
    private JTextField amount;
    private JLabel lblResult;
    private JTextField textFieldResult;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Conversor frame = new Conversor();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Conversor() {
        setIconImage(Toolkit.getDefaultToolkit()
                .getImage("C:\\Users\\Visitante\\Desktop\\Archivos Java\\Conversor_Divisas\\conversor2\\7525685ed67fa782b7d851273e1264c7-cambio-de-divisas.png"));
        setTitle("Conversor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(null);

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnNewButton = new JButton("Change");
        btnNewButton.setBackground(new Color(192, 192, 192));
        btnNewButton.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 12));
        btnNewButton.setBounds(183, 121, 89, 30);
        contentPane.add(btnNewButton);

        amount = new JTextField();
        amount.setBounds(168, 47, 112, 35);
        contentPane.add(amount);
        amount.setColumns(10);

        JComboBox<String> to = new JComboBox<String>();
        to.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16));
        to.setModel(new DefaultComboBoxModel<String>(new String[] { "COP (COL$)", "USD (US$)", "EUR (€)", "GBP (£)",
                "JPY (¥)", "KRW (₩)" }));
        to.setBounds(290, 46, 120, 35);
        contentPane.add(to);

        JComboBox<String> from = new JComboBox<String>();
        from.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16));
        from.setModel(new DefaultComboBoxModel<String>(new String[] { "COP (COL$)", "USD (US$)", "EUR (€)", "GBP (£)",
                "JPY (¥)", "KRW (₩)" }));
        from.setBounds(30, 46, 120, 35);
        contentPane.add(from);

        lblResult = new JLabel("Result");
        lblResult.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 14));
        lblResult.setBounds(96, 198, 54, 20);
        contentPane.add(lblResult);
        
        textFieldResult = new JTextField();
        textFieldResult.setColumns(10);
        textFieldResult.setBounds(168, 199, 112, 35);
        contentPane.add(textFieldResult);

        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fromCurrency = from.getSelectedItem().toString().split(" ")[0];
                String toCurrency = to.getSelectedItem().toString().split(" ")[0];
                double amountValue = Double.parseDouble(amount.getText());

                try {
                    double convertedAmount = convertCurrency(fromCurrency, toCurrency, amountValue);
                    textFieldResult.setText(String.valueOf(convertedAmount));
                } catch (IOException ex) {
                    ex.printStackTrace();
                    textFieldResult.setText("Error: Conversion failed");
                }
            }
        });
    }

    protected double convertCurrency(String fromCurrency, String toCurrency, double amountValue) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        String url = "https://api.apilayer.com/fixer/convert?to=" + toCurrency + "&from=" + fromCurrency + "&amount=" + amountValue;
        Request request = new Request.Builder()
        		.url(url)
                .addHeader("apikey", "bCNJfXoQqaYVKS7JOTH26CCNrGOF2Nb6")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(responseBody);
        
        double convertedAmount = parseResultFromJson(responseBody);
        
        textFieldResult.setText(String.valueOf(convertedAmount));

        return convertedAmount;
    }
    
    private double parseResultFromJson(String json) {
    	try {
            JSONObject jsonObject = new JSONObject(json);
            double result = jsonObject.getDouble("result");
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
