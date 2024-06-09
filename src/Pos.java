import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Pos extends JFrame {
    public Pos() {
        setTitle("굿모닝버거 포스기");
        setSize(1080, 800); // 프레임 크기 1080*800
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임 닫기 설정
        setLocationRelativeTo(null); // 프레임을 화면 중앙에 배치
        setLayout(new BorderLayout()); // BorderLayout 사용

        // 헤더 패널 - 굿모닝 버거 POS
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(Color.DARK_GRAY);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50)); // 높이 50 설정
        JLabel headerLabel = new JLabel("굿모닝버거 POS");
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24)); // 글씨 크기
        headerLabel.setForeground(Color.WHITE); // 글씨 색상을 흰색으로 설정
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(headerLabel);

        // 왼쪽 패널 - 주문표 및 계산
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.setPreferredSize(new Dimension(500, getHeight()));

        // 상단 패널 - 주문표
        JPanel upperLeftPanel = new JPanel(new BorderLayout());
        upperLeftPanel.setBackground(Color.LIGHT_GRAY);
        upperLeftPanel.setPreferredSize(new Dimension(500, 200));

        // 주문표 데이터
        String[] columnNames = {"*", "메뉴명", "단가", "수량", "금액", "비고"};
        Object[][] data = {};

        // 커스텀 테이블 모델 생성
        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // 첫 번째 열(순번)은 편집 불가능
            }

            @Override
            public void addRow(Object[] rowData) {
                Object[] rowWithIndex = new Object[rowData.length + 1];
                rowWithIndex[0] = getRowCount() + 1; // 첫 번째 열에 순번 설정
                System.arraycopy(rowData, 0, rowWithIndex, 1, rowData.length);
                super.addRow(rowWithIndex);
            }
        };

        // 초기 데이터 추가
        for (Object[] row : data) {
            model.addRow(row);
        }

        // JTable 생성
        JTable table = new JTable(model);
        table.setRowHeight(30); // 행 높이
        table.setShowVerticalLines(false); // 세로줄 숨기기

        // 셀 텍스트 중앙 정렬
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        upperLeftPanel.add(scrollPane, BorderLayout.CENTER);

        // 버튼 패널 - 각종 버튼들
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        JButton cancelAllButton = new JButton("전체 취소");
        JButton cancelSelectedButton = new JButton("선택 취소");
        JButton increaseButton = new JButton("+");
        JButton decreaseButton = new JButton("-");
        buttonPanel.add(cancelAllButton);
        buttonPanel.add(cancelSelectedButton);
        buttonPanel.add(increaseButton);
        buttonPanel.add(decreaseButton);

        // 전체 취소 버튼 기능 구현
        cancelAllButton.addActionListener(e -> model.setRowCount(0));

        // 선택 취소 버튼 기능 구현
        cancelSelectedButton.addActionListener(e -> {
            int[] selectedRows = table.getSelectedRows();
            if (selectedRows.length > 0) {
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    model.removeRow(selectedRows[i]);
                }
            }
        });

        // 수량 증가 버튼 기능 구현
        increaseButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int quantity = (int) model.getValueAt(selectedRow, 3);
                int price = (int) model.getValueAt(selectedRow, 2);
                quantity++;
                model.setValueAt(quantity, selectedRow, 3);
                model.setValueAt(quantity * price, selectedRow, 4);
            }
        });

        // 수량 감소 버튼 기능 구현
        decreaseButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int quantity = (int) model.getValueAt(selectedRow, 3);
                int price = (int) model.getValueAt(selectedRow, 2);
                quantity--;
                if (quantity == 0) {
                    model.removeRow(selectedRow);
                } else {
                    model.setValueAt(quantity, selectedRow, 3);
                    model.setValueAt(quantity * price, selectedRow, 4);
                }
            }
        });

        // 하단 패널 - 계산
        JPanel lowerLeftPanel = new JPanel(new BorderLayout());
        lowerLeftPanel.setBackground(Color.LIGHT_GRAY);
        lowerLeftPanel.add(new JLabel("계산"), BorderLayout.NORTH);
        lowerLeftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 왼쪽 패널에 상단 및 하단 패널 추가
        leftPanel.add(upperLeftPanel, BorderLayout.NORTH);

        // 주문표에 버튼 패널 추가
        upperLeftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 왼쪽 패널에 하단 패널 추가
        leftPanel.add(lowerLeftPanel, BorderLayout.CENTER);

        // 오른쪽 패널 - 메뉴판
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.add(new JLabel("메뉴판"), BorderLayout.NORTH);

        // 버튼 패널 생성
        JPanel buttonPanelRight = new JPanel(new GridLayout(4, 3, 0, 0));
        JButton button1 = new JButton("<html><center>앵그리 뉴욕 버거<br>7500원</center></html>");
        JButton button2 = new JButton("<html><center>치즈버거<br>4500원</center></html>");
        JButton button3 = new JButton();
        JButton button4 = new JButton("<html><center>미니언 옐로 머핀<br>3200원</center></html>");
        JButton button5 = new JButton("<html><center>해적왕 루피 머핀<br>4300원</center></html>");
        JButton button6 = new JButton();
        JButton button7 = new JButton("<html><center>아메리카노<br>2000원</center></html>");
        JButton button8 = new JButton("<html><center>바닐라 라떼<br>3500원</center></html>");
        JButton button9 = new JButton("<html><center>카푸치노<br>3200원</center></html>");
        JButton button10 = new JButton("<html><center>카페라떼<br>3000원</center></html>");
        JButton button11 = new JButton("<html><center>오렌지주스<br>1500원</center></html>");
        JButton button12 = new JButton("<html><center>생수<br>1000원</center></html>");

        buttonPanelRight.add(button1);
        buttonPanelRight.add(button2);
        buttonPanelRight.add(button3);
        buttonPanelRight.add(button4);
        buttonPanelRight.add(button5);
        buttonPanelRight.add(button6);
        buttonPanelRight.add(button7);
        buttonPanelRight.add(button8);
        buttonPanelRight.add(button9);
        buttonPanelRight.add(button10);
        buttonPanelRight.add(button11);
        buttonPanelRight.add(button12);

        rightPanel.add(buttonPanelRight, BorderLayout.CENTER);

        // 커피 옵션 선택 다이얼로그
        String[] coffeeOptions = {"Hot", "Iced"};
        String[] sweetnessOptions = {"설탕 x", "덜 달게", "보통", "더 달게"};
        String[] shotOptions = {"1 샷", "2 샷"};

        // 각 버튼에 대한 ActionListener 구현
        button1.addActionListener(e -> {
            Object[] rowData = {"앵그리 뉴욕 버거", 7500, 1, 7500, ""};
            model.addRow(rowData);
        });

        button2.addActionListener(e -> {
            Object[] rowData = {"치즈버거", 4500, 1, 4500, ""};
            model.addRow(rowData);
        });

        button4.addActionListener(e -> {
            Object[] rowData = {"미니언 옐로 머핀", 3200, 1, 3200, ""};
            model.addRow(rowData);
        });

        button5.addActionListener(e -> {
            Object[] rowData = {"해적왕 루피 머핀", 4300, 1, 4300, ""};
            model.addRow(rowData);
        });

        button7.addActionListener(e -> addCoffeeOrder("아메리카노", 2000, model));
        button8.addActionListener(e -> addCoffeeOrder("바닐라 라떼", 3500, model));
        button9.addActionListener(e -> addCoffeeOrder("카푸치노", 3200, model));
        button10.addActionListener(e -> addCoffeeOrder("카페라떼", 3000, model));

        button11.addActionListener(e -> {
            Object[] rowData = {"오렌지주스", 1500, 1, 1500, ""};
            model.addRow(rowData);
        });

        button12.addActionListener(e -> {
            Object[] rowData = {"생수", 1000, 1, 1000, ""};
            model.addRow(rowData);
        });

        // 패널들을 프레임에 추가
        add(headerPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        setVisible(true); // 프레임 출력
    }

    private void addCoffeeOrder(String coffeeName, int basePrice, DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Coffee Options", true);
        dialog.setLayout(new GridLayout(4, 1));

        JPanel tempPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel tempLabel = new JLabel("Temperature:");
        JRadioButton hotButton = new JRadioButton("Hot");
        JRadioButton icedButton = new JRadioButton("Iced");
        ButtonGroup tempGroup = new ButtonGroup();
        tempGroup.add(hotButton);
        tempGroup.add(icedButton);
        hotButton.setSelected(true);
        tempPanel.add(tempLabel);
        tempPanel.add(hotButton);
        tempPanel.add(icedButton);

        JPanel sweetnessPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel sweetnessLabel = new JLabel("달기:");
        JComboBox<String> sweetnessCombo = new JComboBox<>(new String[]{"설탕 x", "덜 달게", "보통", "더 달게"});
        sweetnessPanel.add(sweetnessLabel);
        sweetnessPanel.add(sweetnessCombo);

        JPanel shotPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel shotLabel = new JLabel("샷 추가:");
        JComboBox<String> shotCombo = new JComboBox<>(new String[]{"1 샷", "2 샷"});
        shotPanel.add(shotLabel);
        shotPanel.add(shotCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("추가");
        addButton.addActionListener(e -> {
            String temp = hotButton.isSelected() ? "Hot" : "Iced";
            String sweetness = (String) sweetnessCombo.getSelectedItem();
            String shots = (String) shotCombo.getSelectedItem();
            int shotCount = shots.equals("1 샷") ? 1 : 2;
            int price = basePrice + (temp.equals("Iced") ? 500 : 0) + (shotCount - 1) * 500;

            String orderDetails = coffeeName + " (" + temp + ", " + sweetness + ", " + shots + ")";
            Object[] rowData = {orderDetails, price, 1, price, ""};
            model.addRow(rowData);
            dialog.dispose();
        });
        buttonPanel.add(addButton);

        dialog.add(tempPanel);
        dialog.add(sweetnessPanel);
        dialog.add(shotPanel);
        dialog.add(buttonPanel);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Pos::new);
    }
}
