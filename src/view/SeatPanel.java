package view;

import controller.BookingController;
import controller.SeatController;
import dto.SeatDTO;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SeatPanel extends JDialog {
    private final SeatController seatController;
    // TODO: BookingController 완성 후 주석 해제해야 함
    // private final BookingController bookingController;
    private final List<SeatDTO> seats;

    private JLabel selectedInfoLabel;
    private SeatDTO selectedSeat;

    public SeatPanel(List<SeatDTO> seats, int availableCount, SeatController seatController) {
        this.seats = seats;
        this.seatController = seatController;
        // TODO: BookingController 완성 후 파라미터 추가
        // this.bookingController = bookingController;
        initUI();
    }

    private void initUI() {
        setTitle("SeatPanel");
        setModal(true);
        setSize(650, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 10));

        add(makeLegendPanel(), BorderLayout.NORTH);
        add(makeSeatArea(), BorderLayout.CENTER);
        add(makeBottomPanel(), BorderLayout.SOUTH);
    }

    // 상단 기준 (1~5점 색상)
    private JPanel makeLegendPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        int[] ratings = {1, 2, 3, 4, 5};
        Color[] colors = {
                new Color(220, 50, 30),
                new Color(255, 140, 0),
                new Color(220, 220, 0),
                new Color(120, 220, 50),
                new Color(60, 140, 20)
        };

        for (int i = 0; i < ratings.length; i++) {
            JPanel item = new JPanel(new BorderLayout(0, 4));
            item.setOpaque(false);

            JLabel numLabel = new JLabel(String.valueOf(ratings[i]), SwingConstants.CENTER);
            numLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));

            JPanel colorBox = new JPanel();
            colorBox.setBackground(colors[i]);
            colorBox.setPreferredSize(new Dimension(40, 40));

            item.add(numLabel, BorderLayout.NORTH);
            item.add(colorBox, BorderLayout.CENTER);
            panel.add(item);
        }
        return panel;
    }

    //중앙: STAGE + 구역별 좌석 그리드
    private JPanel makeSeatArea() {
        JPanel area = new JPanel(new BorderLayout(0, 10));
        area.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // STAGE 표시
        JLabel stage = new JLabel("STAGE", SwingConstants.CENTER);
        stage.setOpaque(true);
        stage.setBackground(new Color(200, 200, 200));
        stage.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        stage.setPreferredSize(new Dimension(0, 40));
        area.add(stage, BorderLayout.NORTH);

        // section 기준으로 그룹핑
        Map<String, List<SeatDTO>> sectionMap = new LinkedHashMap<>();
        for (SeatDTO seat : seats) {
            sectionMap.computeIfAbsent(seat.getsection(), k -> new ArrayList<>()).add(seat);
        }

        JPanel sectionsPanel = new JPanel(new GridLayout(1, sectionMap.size(), 10, 0));

        for (Map.Entry<String, List<SeatDTO>> entry : sectionMap.entrySet()) {
            String section = entry.getKey();
            List<SeatDTO> sectionSeats = entry.getValue();

            JPanel sectionPanel = new JPanel(new BorderLayout(0, 5));

            // 구역 라벨 (A / B / C)
            JLabel sectionLabel = new JLabel(section, SwingConstants.CENTER);
            sectionLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
            sectionPanel.add(sectionLabel, BorderLayout.NORTH);

            int maxRow = sectionSeats.stream().mapToInt(SeatDTO::getRowNum).max().orElse(1);
            int maxCol = sectionSeats.stream().mapToInt(SeatDTO::getColNum).max().orElse(1);

            JPanel grid = new JPanel(new GridLayout(maxRow, maxCol, 4, 4));

            sectionSeats.sort(Comparator.comparingInt(SeatDTO::getRowNum)
                    .thenComparingInt(SeatDTO::getColNum));

            for (SeatDTO seat : sectionSeats) {
                grid.add(makeSeatButton(seat));
            }
            sectionPanel.add(grid, BorderLayout.CENTER);
            sectionsPanel.add(sectionPanel);
        }

        area.add(sectionsPanel, BorderLayout.CENTER);
        return area;
    }

    //  좌석 버튼 생성
    private JButton makeSeatButton(SeatDTO seat) {
        JButton btn = new JButton("<html><center>"
                + seat.getRowNum() + "<br>" + seat.getColNum()
                + "</center></html>");
        btn.setPreferredSize(new Dimension(45, 45));
        btn.setBackground(getColor(seat.getColor()));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFont(new Font("맑은 고딕", Font.PLAIN, 10));

        btn.addActionListener(e -> {
            selectedSeat = seat;
            updateSelectedInfo(seat);
            seatController.onSeatSelected(seat);
        });

        return btn;
    }

    //하단: 선택 좌석 정보 + 결제 버튼
    private JPanel makeBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));

        selectedInfoLabel = new JLabel("좌석을 선택해주세요.", SwingConstants.CENTER);
        selectedInfoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        panel.add(selectedInfoLabel, BorderLayout.CENTER);

        JButton payBtn = new JButton("결제하기");
        payBtn.setBackground(new Color(70, 130, 230));
        payBtn.setForeground(Color.WHITE);
        payBtn.setOpaque(true);
        payBtn.setBorderPainted(false);
        payBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        payBtn.setPreferredSize(new Dimension(150, 40));

        payBtn.addActionListener(e -> {
            if (selectedSeat == null) {
                JOptionPane.showMessageDialog(this, "좌석을 먼저 선택해주세요.");
                return;
            }
            // TODO: BookingController 완성 후 주석 해제
            // bookingController.startBooking(selectedSeat);
            // dispose();
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(payBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // 선택 좌석 정보 라벨 업데이트
    private void updateSelectedInfo(SeatDTO seat) {
        String info = String.format("선택 : %s구역 %d행 %d열  |  ★ %.1f점",
                seat.getsection(), seat.getRowNum(), seat.getColNum(), seat.getAvgRating());
        selectedInfoLabel.setText(info);
    }

    // 색상 문자열 -> Color 변환
    private Color getColor(String color) {
        return switch (color) {
            case "RED"    -> new Color(220, 50, 30);
            case "ORANGE" -> new Color(255, 140, 0);
            case "YELLOW" -> new Color(220, 220, 0);
            case "LIME"   -> new Color(120, 220, 50);
            case "GREEN"  -> new Color(60, 140, 20);
            default       -> Color.LIGHT_GRAY;
        };
    }
}