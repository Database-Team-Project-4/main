package twitter.ui.mainPage;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicScrollBarUI;

import twitter.ui.follow.follower.FollowerListPanel;
import twitter.ui.follow.following.FollowingListPanel;
import twitter.ui.mainPage.FollowerTopPanel;
import twitter.main.MainFrame;
import twitter.service.userService;
import twitter.ui.post.PostUI;
import twitter.ui.post.*;

public class Main_Ui extends JPanel {
    private JPanel completeTopPanel;
    private JButton homeButton, searchButton, followerButton, bookmarkButton;
    private JPanel mainPanel;
    private JPanel bottomPanel;

    public JPanel getMainPanel() {
        return mainPanel;
    }


    private final String homeIconDefault = "/TwitterIcons/home_icondef.png";
    private final String homeIconClicked = "/TwitterIcons/home_iconclicked.png";
    private final String homeIconHover = "/TwitterIcons/home_iconcursor.png";
    private final String searchIconDefault = "/TwitterIcons/searchdef.png";
    private final String searchIconHover = "/TwitterIcons/searchcursor.png";
    private final String searchIconClicked = "/TwitterIcons/searchclicked.png";
    private final String communityIconDefault = "/TwitterIcons/comdef.png";
    private final String communityIconHover = "/TwitterIcons/comcursor.png";
    private final String communityIconClicked = "/TwitterIcons/comclicked.png";
    private final String BookmarkIconDefault = "/TwitterIcons/bookmarkdef.png";
    private final String BookmarkIconHover = "/TwitterIcons/bookmarkdef.png";
    private final String BookmarkIconClicked = "/TwitterIcons/bookmarkClicked.png";

    public Main_Ui(MainFrame mainframe, Connection connection, userService userService) {
        setLayout(new BorderLayout());

        completeTopPanel = new JPanel(new CardLayout());
        completeTopPanel.add(new MainTopPanel(this, mainframe, connection, userService), "MainTop");
        completeTopPanel.add(new SearchTopPanel(mainframe, connection, userService), "SearchTop");

        //completeTopPanel.add(new FollowerTopPanel("강동호/AIㆍ소프트웨어학부(인공지능전공)"), "FollowerTop"); //시현용입니다.
        completeTopPanel.add(new FollowerTopPanel(mainframe, connection, userService, this), "FollowerTop"); //--> 데이터베이스에서 정보를 가져오는 방식입니다.

        completeTopPanel.add(new FollowerTopPanel(mainframe, connection, userService, this), "FollowerTop"); //FollowerTopPanel에서 mainui의 메소드를 사용하기위해 this를 넘겼습니다.

        completeTopPanel.add(new BookmarkTopPanel(), "BookmarkTop");

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(7, 7, 7));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setPreferredSize(new Dimension(getWidth(), 72));

        homeButton = createIconButtonWithHover(homeIconDefault, homeIconHover, homeIconClicked);
        searchButton = createIconButtonWithHover(searchIconDefault, searchIconHover, searchIconClicked);
        followerButton = createIconButtonWithHover(communityIconDefault, communityIconHover, communityIconClicked);
        bookmarkButton = createIconButtonWithHover(BookmarkIconDefault, BookmarkIconHover, BookmarkIconClicked);
        homeButton.addActionListener(e -> {
            setBottomButtonSelected(homeButton);
            showPanel("MainTop");
        });

        searchButton.addActionListener(e -> {
            setBottomButtonSelected(searchButton);
            showPanel("SearchTop");
        });

        followerButton.addActionListener(e -> {
            setBottomButtonSelected(followerButton);
            showPanel("FollowerTop");
        });

        bookmarkButton.addActionListener(e -> {
            setBottomButtonSelected(bookmarkButton);
            showPanel("BookmarkTop"); // 북마크 버튼 클릭 시 BookmarkTop 패널 표시
        });


        bottomPanel.add(homeButton);
        bottomPanel.add(searchButton);
        bottomPanel.add(followerButton);
        bottomPanel.add(bookmarkButton);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(LEFT_ALIGNMENT); // 패널을 왼쪽 정렬로 설정

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

   // 커스텀 스크롤바 디자인 적용
   scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = new Color(128, 128, 128, 180); // 회색, 반투명
    }
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createEmptyButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createEmptyButton();
    }

    private JButton createEmptyButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(thumbColor);
        g2.fillRoundRect(thumbBounds.x, thumbBounds.y, 8, thumbBounds.height, 10, 10); // 스크롤 막대 너비 8로 설정
        g2.dispose();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        // 트랙을 투명하게 처리하여 보이지 않도록 설정
        g.setColor(Color.BLACK); // 완전히 투명한 색
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }
});


// 스크롤바 두께 설정 및 가로 스크롤바 숨김
scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

add(completeTopPanel, BorderLayout.NORTH);
add(scrollPane, BorderLayout.CENTER);
add(bottomPanel, BorderLayout.SOUTH);

setBottomButtonSelected(homeButton);
updatePostContent("recommend");


}


    public void updatePostContent(String type) {
        mainPanel.removeAll(); // 기존 콘텐츠를 지우고 새로운 콘텐츠로 업데이트

        // 테스트용 임시 데이터로 PostUI 인스턴스를 추가
        if (type.equals("recommend")) {
            mainPanel.add(new PostUI("Tom", "@tom", "Test content from Tom", 10, 2, 3));
            mainPanel.add(new PostUI("Lud", "@lud", "Following content from Lud", 8, 4, 1));
            mainPanel.add(new PostUI("Kim", "@kim", "Hello world!", 5, 1, 0));
            mainPanel.add(new PostUI("Jun", "@jun", "Good day everyone!", 15, 7, 2));

        } else if (type.equals("following")) {
            mainPanel.add(new PostUI("Lud", "@lud", "Following content from Lud", 8, 4, 1));
            mainPanel.add(new PostUI("Jun", "@jun", "Good day everyone!", 15, 7, 2));
        }
        int postCount = mainPanel.getComponentCount();
        int postHeight = 150; // 각 포스트의 예상 높이 (150px 예시)

        mainPanel.setPreferredSize(new Dimension(getWidth(), postCount * postHeight + 80));

        mainPanel.revalidate(); // 레이아웃 업데이트
        mainPanel.repaint(); // 화면 갱신

         JScrollPane scrollPane = (JScrollPane) mainPanel.getParent().getParent();
         SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }




    // 추천 및 팔로우 중에 따른 포스트 내용 갱신 메서드 -> 데이터베이스에서 불러옴
    /**public void updatePostContent(String type) {
        postPanel.removeAll();

        String query;
        if (type.equals("recommend")) {
            // 전체 게시글을 순서대로 가져오기
            query = "SELECT post_id FROM posts ORDER BY created_at DESC";
        } else {
            // 특정 유저들의 게시글만 가져오기 (Tom과 Lud의 게시글만)
            query = "SELECT post_id FROM posts WHERE user_id IN " +
                    "(SELECT user_id FROM users WHERE name IN ('Tom', 'Lud')) ORDER BY created_at DESC";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int postId = rs.getInt("post_id");
                postPanel.add(new PostUI(postId, connection));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
             int postCount = postPanel.getComponentCount();
        int postHeight = 150; // 각 포스트의 예상 높이 (150px 예시)

        postPanel.setPreferredSize(new Dimension(getWidth(), postCount * postHeight + 80));

        postPanel.revalidate(); // 레이아웃 업데이트
        postPanel.repaint(); // 화면 갱신

         JScrollPane scrollPane = (JScrollPane) postPanel.getParent().getParent();
         SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));


    }
        **/

    public void updateSearchContent(String keyword, String filterType) {

        mainPanel.removeAll(); // 콘텐츠 제거
        if (keyword == null || keyword.trim().isEmpty()) {
            // 검색어가 없을 때 "검색어 없음" 메시지 출력
            System.out.println("검색어가 비어 있습니다. '검색어 없음' 메시지를 표시합니다.");
            JPanel noKeywordPanel = new JPanel();
            noKeywordPanel.setBackground(Color.BLACK);
            noKeywordPanel.setLayout(new GridBagLayout());
            JLabel noKeywordLabel = new JLabel("검색어 없음");
            noKeywordLabel.setForeground(Color.WHITE);
            noKeywordLabel.setFont(new Font("Arial", Font.BOLD, 20));
            noKeywordPanel.add(noKeywordLabel);

            mainPanel.add(noKeywordPanel); // 메인 패널에 추가
        } else {
        // 예시 데이터
        List<PostUI> examplePosts = Arrays.asList(

                new PostUI("Kim", "@kim", "Hello world!", 5, 1, 0),
                new PostUI("Lud", "@lud", "Following content from Lud", 8, 4, 1),
                new PostUI("Tom", "@tom", "Test content from Tom", 10, 2, 3),
                new PostUI("Tom", "@tom", "This is a test post about Java programming.", 15, 5, 2),
                new PostUI("Lud", "@lud", "Learning Swing in Java!", 10, 3, 1),
                new PostUI("Kim", "@kim", "Search functionality is great.", 20, 7, 5),
                new PostUI("Jun", "@jun", "Working on a Java project today.", 5, 2, 0),
                new PostUI("Alice", "@alice", "I love coding in Java!", 25, 10, 7),
                new PostUI("Bob", "@bob", "Designing user interfaces is fun.", 12, 4, 1),
                new PostUI("Charlie", "@charlie", "Java is versatile and powerful.", 18, 6, 3),
                new PostUI("Dave", "@dave", "How to implement search in Java apps?", 8, 3, 0)
        );


            // 키워드를 포함하는 포스트 필터링
            List<PostUI> filteredPosts = new ArrayList<>(examplePosts.stream()
                    .filter(post -> post.getContentText().toLowerCase().contains(keyword.toLowerCase()) ||
                            post.getUserName().toLowerCase().contains(keyword.toLowerCase()) ||
                            post.getUserHandle().toLowerCase().contains(keyword.toLowerCase()))
                    .toList());

            filteredPosts.forEach(post -> System.out.println(post.getContentText()));


            mainPanel.revalidate();
            mainPanel.repaint();

            // 필터링 결과를 인기순/최신순으로 정렬
            if ("popular".equals(filterType)) {
                filteredPosts.sort((p1, p2) -> Integer.compare(p2.getLikes(), p1.getLikes())); // 좋아요 수로 정렬
            } else if ("recent".equals(filterType)) {
                // 최신순 정렬. 예시 데이터에 최신 정보를 추가하지 않았으므로 생략
                // filteredPosts.sort((p1, p2) -> p2.getCreationTime().compareTo(p1.getCreationTime()));
            }
            //filteredPosts.forEach(mainPanel::add);

            // 필터링된 결과를 메인 패널에 추가
            for (PostUI post : filteredPosts) {
                mainPanel.add(post);
                System.out.println("added post: " + post.getContentText());
            }

            // 동적 크기 조정 및 레이아웃 업데이트
            int postCount = mainPanel.getComponentCount();
            int postHeight = 150; // 각 포스트의 예상 높이
            mainPanel.setPreferredSize(new Dimension(getWidth(), postCount * postHeight + 72));

            mainPanel.revalidate(); // 레이아웃 업데이트
            mainPanel.repaint(); // 화면 갱신

            showPanel("SearchTop");


            for (Component c : getComponents()) {
                System.out.println(c.getClass().getName());
            }

            for (Component comp : mainPanel.getComponents()) {
                System.out.println(comp.getClass().getName());
            }

            // 스크롤바 위치 초기화
            JScrollPane scrollPane = (JScrollPane) mainPanel.getParent().getParent();
            SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));

        }
    }

    public void updateFollowContent(String type) {
        mainPanel.removeAll();
        /*
        DB에서 follow관련 데이터를 가져와야합니다.
         */
        String[] userNameArray = {
                "tvN drama", "KBS DRAMA", "TVING", "쿠팡플레이", "Netflix K-Content",
                "tvN drama", "KBS DRAMA", "TVING", "쿠팡플레이", "Netflix K-Content"
        };
        String[] userHandleArray = {
                "tvN drama", "KBS DRAMA", "TVING", "쿠팡플레이", "Netflix K-Content",
                "tvN drama", "KBS DRAMA", "TVING", "쿠팡플레이", "Netflix K-Content"
        };

        List<String> userNames = Arrays.asList(userNameArray);
        List<String> userHandles = Arrays.asList(userHandleArray);
        ImageIcon profileImage = new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png"));
        if (type.equals("follower")) {
            System.out.println("follower화면으로 바꿨어요"); //debug code
            mainPanel.add(new FollowerListPanel(userNames,userHandles,profileImage));
        } else if (type.equals("following")){
            System.out.println("following화면으로 바꿨어요"); //debug code
            mainPanel.add(new FollowingListPanel(userNames,userHandles,profileImage));
        }

        //postPanel.setPreferredSize(new Dimension(getWidth(), postCount * postHeight + 80));

        mainPanel.revalidate(); // 레이아웃 업데이트
        mainPanel.repaint(); // 화면 갱신

        JScrollPane scrollPane = (JScrollPane) mainPanel.getParent().getParent();
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }

    private JButton createIconButtonWithHover(String defaultIconPath, String hoverIconPath, String clickedIconPath) {
        ImageIcon defaultIcon = loadIcon(defaultIconPath);
        ImageIcon hoverIcon = loadIcon(hoverIconPath);
        ImageIcon clickedIcon = loadIcon(clickedIconPath);

        JButton button = new JButton(defaultIcon);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        button.addMouseListener(new IconButtonMouseAdapter(button, hoverIcon, defaultIcon, clickedIcon));
        return button;
    }

    private ImageIcon loadIcon(String iconPath) {
        java.net.URL resource = getClass().getResource(iconPath);
        return resource != null ? new ImageIcon(resource) : null;
    }

    private void setBottomButtonSelected(JButton selectedButton) {
        // 모든 버튼의 아이콘을 초기화하여 기본 상태로 설정
        homeButton.setIcon(loadIcon(homeIconDefault));
        searchButton.setIcon(loadIcon(searchIconDefault));
        followerButton.setIcon(loadIcon(communityIconDefault));
        bookmarkButton.setIcon(loadIcon(BookmarkIconDefault));

        // 클릭된 버튼만 클릭된 상태의 아이콘으로 설정
        if (selectedButton == homeButton) {
            selectedButton.setIcon(loadIcon(homeIconClicked));
        } else if (selectedButton == searchButton) {
            selectedButton.setIcon(loadIcon(searchIconClicked));
        } else if (selectedButton == followerButton) {
            selectedButton.setIcon(loadIcon(communityIconClicked));
        } else if (selectedButton == bookmarkButton) {
            selectedButton.setIcon(loadIcon(BookmarkIconClicked));
        }

        // 선택된 버튼 텍스트 색상을 흰색으로 설정하고 나머지는 기본색으로 초기화
        homeButton.setForeground(Color.GRAY);
        searchButton.setForeground(Color.GRAY);
        followerButton.setForeground(Color.GRAY);
        bookmarkButton.setForeground(Color.GRAY);

        selectedButton.setForeground(Color.WHITE);
    }
    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (completeTopPanel.getLayout());
        cl.show(completeTopPanel, panelName); // 패널 전환

        if ("MainTop".equals(panelName)) {
            updatePostContent("recommend");
        } else if ("FollowerTop".equals(panelName)) {
            updateFollowContent("follower");
        } else if ("SearchTop".equals(panelName)) {
            System.out.println("SearchTop 패널 표시");
            SearchTopPanel searchTopPanel = (SearchTopPanel) completeTopPanel.getComponent(1); // SearchTopPanel 가져오기
            searchTopPanel.addSearchListener(() -> {
                updateSearchContent("", "default");
                String keyword = searchTopPanel.getKeyword(); // 검색어 가져오기
                String filterType = searchTopPanel.getCurrentFilterType(); // 필터 타입 가져오기

                // updateSearchContent 호출로 mainPanel 갱신
                updateSearchContent(keyword, filterType);

                // 갱신된 mainPanel 출력
                mainPanel.revalidate();
                mainPanel.repaint();

                JScrollPane scrollPane = (JScrollPane) mainPanel.getParent().getParent();
                SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0)); // 스크롤 초기화

            });

        } else {
            mainPanel.removeAll(); // 다른 패널에서는 초기화
            mainPanel.revalidate();
            mainPanel.repaint();
        }

        // 현재 활성화된 패널 디버그 출력

    }


    public void refreshMainPanel() {
        mainPanel.revalidate();
        mainPanel.repaint();

        JScrollPane scrollPane = (JScrollPane) mainPanel.getParent().getParent();
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0)); // 스크롤 초기
        System.out.println("mainPanel Refreshed!!");
    }


    private class IconButtonMouseAdapter extends MouseAdapter {
        private final JButton button;
        private final ImageIcon hoverIcon;
        private final ImageIcon defaultIcon;
        private final ImageIcon clickedIcon;
        private boolean isClicked;

        public IconButtonMouseAdapter(JButton button, ImageIcon hoverIcon, ImageIcon defaultIcon, ImageIcon clickedIcon) {
            this.button = button;
            this.hoverIcon = hoverIcon;
            this.defaultIcon = defaultIcon;
            this.clickedIcon = clickedIcon;
            this.isClicked = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!isClicked && hoverIcon != null) {
                button.setIcon(hoverIcon);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!isClicked && defaultIcon != null) {
                button.setIcon(defaultIcon);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            button.setIcon(clickedIcon);
            isClicked = true;
        }
    }
}