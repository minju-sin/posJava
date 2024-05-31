import javax.swing.*;
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
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50)); // 높이 50 설정
        JLabel headerLabel = new JLabel("굿모닝버거 POS");
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24)); // 글씨 크기 24로 설정
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(headerLabel);

        // 왼쪽 패널 - 주문표 및 계산
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.setPreferredSize(new Dimension(500, getHeight())); // 너비 500 설정

        // 상단 패널 - 주문표
        JPanel upperLeftPanel = new JPanel();
        upperLeftPanel.setBackground(Color.LIGHT_GRAY);
        upperLeftPanel.setPreferredSize(new Dimension(500, 400)); // 높이 400 설정
        upperLeftPanel.add(new JLabel("주문표"));

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
