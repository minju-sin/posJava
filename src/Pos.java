import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;

public class Pos extends JFrame {

    private JLabel totalMoney;

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
        totalMoney = new JLabel("0"); // 라벨을 초기화합니다.
        upperLeftPanel.setBackground(Color.LIGHT_GRAY);
        upperLeftPanel.setPreferredSize(new Dimension(500, 200));


        // 주문표 데이터
        String[] columnNames = {
                "*",
                "메뉴명",
                "단가",
                "수량",
                "금액",
                "비고"
        };
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
                updateTotalMoney(this, totalMoney); // 행이 추가될 때 총금액 업데이트
            }

            @Override
            public void removeRow(int row) {
                super.removeRow(row);
                updateTotalMoney(this, totalMoney); // 행이 제거될 때 총금액 업데이트
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
                updateTotalMoney(model, totalMoney); // 수량이 변경될 때 총금액 업데이트
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

                }

                else {
                    model.setValueAt(quantity, selectedRow, 3);
                    model.setValueAt(quantity * price, selectedRow, 4);
                }
                updateTotalMoney(model, totalMoney); // 수량이 변경될 때 총금액 업데이트
            }
        });


        // 하단 패널 - 계산

        // 하단 왼쪽 패널 - 계산 결과값
        JPanel aPanel = new JPanel(new GridLayout(5,2));
        aPanel.setPreferredSize(new Dimension(240, 200));
        aPanel.setBorder(new EmptyBorder(5, 10, 5, 15)); // 상단, 좌측, 하단, 우측 여백 설정
        
        
        // 총금액
        JLabel totalLabel = new JLabel("총금액");
        totalLabel.setFont(new Font("Serif", Font.PLAIN, 25));

        aPanel.add(totalLabel);

        JLabel totalMoney = new JLabel("0");
        totalMoney.setHorizontalAlignment(SwingConstants.RIGHT);
        totalMoney.setFont(new Font("Serif", Font.PLAIN, 30));

        aPanel.add(totalMoney);

        
        // 할인금액
        JLabel saleLabel = new JLabel("할인금액");
        saleLabel.setFont(new Font("Serif", Font.PLAIN, 25));

        aPanel.add(saleLabel);

        JLabel saleMoney = new JLabel("0");
        saleMoney.setHorizontalAlignment(SwingConstants.RIGHT);
        saleMoney.setFont(new Font("Serif", Font.PLAIN, 30));

        aPanel.add(saleMoney);

        
        // 받을금액
        JLabel receiveLabel = new JLabel("받을금액");
        receiveLabel.setFont(new Font("Serif", Font.PLAIN, 25));

        aPanel.add(receiveLabel);

        JLabel receiveMoney = new JLabel(totalMoney.getText());
        receiveMoney.setHorizontalAlignment(SwingConstants.RIGHT);
        receiveMoney.setFont(new Font("Serif", Font.PLAIN, 30));

        aPanel.add(receiveMoney);

        
        // 받은금액
        JLabel receiveLabel2 = new JLabel("받은금액");
        receiveLabel2.setFont(new Font("Serif", Font.PLAIN, 25));

        aPanel.add(receiveLabel2);

        JLabel receiveMoney2 = new JLabel("0");
        receiveMoney2.setHorizontalAlignment(SwingConstants.RIGHT);
        receiveMoney2.setFont(new Font("Serif", Font.PLAIN, 30));

        aPanel.add(receiveMoney2);

        
        // 거스름돈
        JLabel changeLabel = new JLabel("거스름돈");
        changeLabel.setFont(new Font("Serif", Font.PLAIN, 25));

        aPanel.add(changeLabel);

        JLabel changeMoney = new JLabel("0");
        changeMoney.setHorizontalAlignment(SwingConstants.RIGHT);
        changeMoney.setFont(new Font("Serif", Font.PLAIN, 30));

        aPanel.add(changeMoney);



        // 하단 오른쪽 패널
        JPanel bPanel = new JPanel();
        bPanel.setPreferredSize(new Dimension(260, 550));



        // 하단 오른쪽-위 패널 계산기 버튼
        JPanel cPanel = new JPanel(new GridBagLayout());
        cPanel.setPreferredSize(new Dimension(260, 400));


        GridBagConstraints gbc = new GridBagConstraints();


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // result 버튼이 3열을 차지하도록 함
        gbc.insets = new Insets(5, 5, 5, 5); // 여백 조절


        JButton calResult=new JButton("0");
        calResult.setPreferredSize(new Dimension(230, 50)); // 버튼 크기 설정
        calResult.setHorizontalAlignment(SwingConstants.RIGHT); // 텍스트 우측 정렬 설정
        cPanel.add(calResult,gbc);



        // 계산기 숫자 버튼
        String[] texts = {
                "7",
                "8",
                "9",
                "4",
                "5",
                "6",
                "1",
                "2",
                "3",
                "0",
                "00",
                "C",
                "<",
                "할인",
                "취소"
        };


        for (int i = 0; i < texts.length; i++) {

            String text = texts[i];
            JButton button = new JButton(texts[i]);

            gbc = new GridBagConstraints(); // 새로운 GridBagConstraints 생성

            gbc.gridx = i % 3; // 3개의 열에 순환 배치
            gbc.gridy = i / 3 + 1; // result 버튼 다음 행에 배치

            gbc.insets = new Insets(5, 5, 5, 5); // 여백 조절

            button.setPreferredSize(new Dimension(70, 50)); // 버튼 크기 설정

            cPanel.add(button, gbc);


            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    int currentValue = Integer.parseInt(calResult.getText());

                    int newValue;
                    int saleMode;

                    if(totalMoney.getText().equals("0")){

                        currentValue=0;

                    }


                    String currentText = calResult.getText();
                    int textLength = currentText.length();

                    if (text.equals("C")) {

                        // Clear 버튼 누를 때, 결과 초기화
                        calResult.setText("0");

                    }
                    else if(text.equals("<")&&textLength > 0){

                        String newText = currentText.substring(0, textLength - 1);

                        // 만약 모든 숫자를 지우면 "0"으로 설정
                        if (newText.isEmpty()) {

                            calResult.setText("0");

                        }

                        else {

                            calResult.setText(newText);

                        }
                    }


                    else if(text.equals("할인")&&!calResult.getText().equals("0")&&
                            Integer.parseInt(calResult.getText())<Integer.parseInt(totalMoney.getText())){

                        int totalSale=currentValue+Integer.parseInt(saleMoney.getText());
                        saleMoney.setText(Integer.toString(totalSale));
                        calResult.setText("0");

                        // totalMoney의 현재 값을 가져와서 정수로 변환하여 사용
                        int currentTotal = Integer.parseInt(totalMoney.getText());
                        int tMoney = currentTotal - Integer.parseInt(saleMoney.getText());

                        // totalMoney 라벨의 텍스트를 변경
                        totalMoney.setText(Integer.toString(tMoney));

                        // receiveMoney 라벨에도 동일한 값 설정
                        receiveMoney.setText(totalMoney.getText());


                    }


                    else if(text.equals("취소")){

                        saleMoney.setText("0");
                        totalMoney.setText("0");
                        receiveMoney.setText(totalMoney.getText());
                        receiveMoney2.setText("0");
                        changeMoney.setText("0");

                    }


                    else {

                        try {

                            if (text.equals("00")) {

                                newValue = currentValue * 100;

                            }

                            else {

                                newValue = currentValue * 10 + Integer.parseInt(text);

                            }


                            calResult.setText(Integer.toString(newValue));
                            currentValue=newValue;

                        }

                        catch (NumberFormatException ex) {
                            // 숫자로 변환할 수 없는 경우 무시

                        }
                    }


                }
            });
        }



        // 하단 오른쪽-아래 패널 - 현금/카드 결제
        JPanel dPanel = new JPanel(new GridBagLayout());
        dPanel.setPreferredSize(new Dimension(260, 100));

        GridBagConstraints dgbc = new GridBagConstraints();


        // 기본 설정
        dgbc.fill = GridBagConstraints.NONE;
        dgbc.insets = new Insets(0, 0, 0, 0); // 기본 패딩 설정


        JButton cash = new JButton("현금");
        dgbc.gridx = 0;
        dgbc.gridy = 0;
        dgbc.insets = new Insets(0, 5, 5, 5);
        dPanel.add(cash, dgbc);
        cash.setPreferredSize(new Dimension(110, 85)); // 버튼 크기 설정


        JButton card = new JButton("카드");
        dgbc.gridx = 1;
        dgbc.gridy = 0;
        dgbc.insets = new Insets(0, 5, 5, 5);
        dPanel.add(card, dgbc);
        card.setPreferredSize(new Dimension(110, 85)); // 버튼 크기 설정


        // 현금 버튼에 액션 리스너 추가
        cash.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if(!calResult.getText().equals("0")){

                    int cashMoneyValue=Integer.parseInt(calResult.getText())+Integer.parseInt(receiveMoney2.getText());
                    int cashMoney= Integer.parseInt(receiveMoney.getText())-Integer.parseInt(calResult.getText());


                    if(Integer.parseInt(calResult.getText())<Integer.parseInt(receiveMoney.getText())){

                        receiveMoney.setText(Integer.toString(cashMoney));
                        receiveMoney2.setText(Integer.toString(cashMoneyValue));
                        calResult.setText("0");

                    }


                    else{

                        int changeMoneyValue=Integer.parseInt(calResult.getText())-Integer.parseInt(receiveMoney.getText());
                        receiveMoney2.setText(Integer.toString(cashMoneyValue));
                        changeMoney.setText(Integer.toString(changeMoneyValue));
                        calResult.setText("0");

                    }
                }


                else{

                    receiveMoney2.setText(totalMoney.getText());

                }
            }
        });


        // 카드 버튼에 액션 리스너 추가
        card.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if(!calResult.getText().equals("0")){

                    if(Integer.parseInt(calResult.getText())<Integer.parseInt(receiveMoney.getText())){

                        int cardMoneyValue=Integer.parseInt(calResult.getText())+Integer.parseInt(receiveMoney2.getText());
                        int cardMoney= Integer.parseInt(receiveMoney.getText())-Integer.parseInt(calResult.getText());

                        receiveMoney.setText(Integer.toString(cardMoney));
                        receiveMoney2.setText(Integer.toString(cardMoneyValue));
                        calResult.setText("0");

                    }
                }


                else{

                    receiveMoney2.setText(totalMoney.getText());

                }
            }
        });


        // 왼쪽 패널에 상단 및 하단 패널 추가
        leftPanel.add(upperLeftPanel, BorderLayout.NORTH);


        // 주문표에 버튼 패널 추가
        upperLeftPanel.add(buttonPanel, BorderLayout.SOUTH);


        // 왼쪽 패널에 하단-왼쪽 패널 추가 (결과값)
        leftPanel.add(aPanel, BorderLayout.WEST);


        // 왼쪽 패널 하단-오른쪽 패널 추가 (빈공간)
        leftPanel.add(bPanel, BorderLayout.EAST);


        // 왼쪽 패널에 하단-오른쪽-위 패널 추가 (계산기 버튼)
        bPanel.add(cPanel, BorderLayout.NORTH);


        // 왼쪽 패널에 하단-오른쪽-아래 패널 추가 (현금/카드결제)
        bPanel.add(dPanel,BorderLayout.SOUTH);


        // 오른쪽 패널 - 메뉴판
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.LIGHT_GRAY);



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
        String[] coffeeOptions = {
                "Hot",
                "Iced"
        };

        String[] sweetnessOptions = {
                "설탕 x",
                "덜 달게",
                "보통",
                "더 달게"
        };

        String[] shotOptions = {
                "1 샷",
                "2 샷",
                "3 샷",
                "4 샷"

        };


        // 각 버튼에 대한 ActionListener 구현
        button1.addActionListener(e -> {

            Object[] rowData = {"앵그리 뉴욕 버거", 7500, 1, 7500, ""};
            model.addRow(rowData);
            updateTotalMoney(model, totalMoney); // 상품이 추가될 때 총금액 업데이트

        });

        button2.addActionListener(e -> {

            Object[] rowData = {"치즈버거", 4500, 1, 4500, ""};
            model.addRow(rowData);
            updateTotalMoney(model, totalMoney); // 상품이 추가될 때 총금액 업데이트

        });

        button4.addActionListener(e -> {

            Object[] rowData = {"미니언 옐로 머핀", 3200, 1, 3200, ""};
            model.addRow(rowData);
            updateTotalMoney(model, totalMoney); // 상품이 추가될 때 총금액 업데이트

        });

        button5.addActionListener(e -> {

            Object[] rowData = {"해적왕 루피 머핀", 4300, 1, 4300, ""};
            model.addRow(rowData);
            updateTotalMoney(model, totalMoney); // 상품이 추가될 때 총금액 업데이트

        });


        button7.addActionListener(e -> addCoffeeOrder("아메리카노", 2000, model));
        button8.addActionListener(e -> addCoffeeOrder("바닐라 라떼", 3500, model));
        button9.addActionListener(e -> addCoffeeOrder("카푸치노", 3200, model));
        button10.addActionListener(e -> addCoffeeOrder("카페라떼", 3000, model));


        button11.addActionListener(e -> {

            Object[] rowData = {"오렌지주스", 1500, 1, 1500, ""};
            model.addRow(rowData);
            updateTotalMoney(model, totalMoney); // 상품이 추가될 때 총금액 업데이트

        });

        button12.addActionListener(e -> {

            Object[] rowData = {"생수", 1000, 1, 1000, ""};
            model.addRow(rowData);
            updateTotalMoney(model, totalMoney); // 상품이 추가될 때 총금액 업데이트

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
        JComboBox<String> shotCombo = new JComboBox<>(new String[]{"1 샷", "2 샷", "3 샷","4 샷"});
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
            updateTotalMoney(model, totalMoney); // 상품이 추가될 때 총금액 업데이트
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

    private void updateTotalMoney(DefaultTableModel model, JLabel totalMoney) {
        int total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            total += (int) model.getValueAt(i, 4); // 금액 열의 값을 더함
        }
        totalMoney.setText(String.valueOf(total));
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(Pos::new);

    }
}
