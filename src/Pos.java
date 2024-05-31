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
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24)); // 글씨 크기 24로 설정
        headerLabel.setForeground(Color.WHITE); // 글씨 색상을 흰색으로 설정
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(headerLabel);

        // 왼쪽 패널 - 주문표 및 계산
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.setPreferredSize(new Dimension(500, getHeight())); // 너비 500 설정

        // 상단 패널 - 주문표
        JPanel upperLeftPanel = new JPanel(new BorderLayout());
        upperLeftPanel.setBackground(Color.LIGHT_GRAY);
        upperLeftPanel.setPreferredSize(new Dimension(500, 300)); // 높이 400 설정

        // 주문표 데이터 임시로 넣은거임
        String[] columnNames = {"*", "메뉴명", "단가", "수량", "금액", "비고"};
        Object[][] data = {
                {"햄버거", 5000, 1, 5000, ""},
                {"감자튀김", 3000, 2, 6000, ""},
                {"콜라", 2000, 1, 2000, ""}
        };

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

        // 주문표 테이블 생성하기
        JTable table = new JTable(model);
        table.setRowHeight(30); // 행 높이를 30으로 설정
        table.setShowVerticalLines(false); // 세로줄 숨기기

        // 주문표 셀 텍스트 중앙 정렬
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        upperLeftPanel.add(scrollPane, BorderLayout.CENTER);

        // 하단 패널 - 계산
        JPanel lowerLeftPanel = new JPanel();
        lowerLeftPanel.setBackground(Color.LIGHT_GRAY);
        lowerLeftPanel.add(new JLabel("계산"));

        // 왼쪽 패널에 상단 및 하단 패널 추가
        leftPanel.add(upperLeftPanel, BorderLayout.NORTH);
        leftPanel.add(lowerLeftPanel, BorderLayout.CENTER);

        // 오른쪽 패널 - 메뉴판 
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.add(new JLabel("메뉴판"));

        // 패널들을 프레임에 추가
        add(headerPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        setVisible(true); // 프레임 출력
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Pos();
        });
    }
}
