import java.util.*;

// Abstract Tweet class (Abstraction)
abstract class AbstractTweet {
    protected final int id;
    protected final String message;
    protected final String postedBy;
    protected final long timestamp;

    public AbstractTweet(int id, String message, String postedBy) {
        this.id = id;
        this.message = message;
        this.postedBy = postedBy;
        this.timestamp = System.currentTimeMillis();
    }

    public abstract void display();
}

// Concrete Tweet class (Inheritance)
class Tweet extends AbstractTweet {
    private int likes;

    public Tweet(int id, String message, String postedBy) {
        super(id, message, postedBy);
        this.likes = 0;
    }

    public void like() {
        likes++;
    }

    public int getLikes() {
        return likes;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void display() {
        System.out.println("[" + id + "] " + postedBy + ": " + message + " ❤️ " + likes);
    }
}

// User class (Encapsulation)
class User {
    private final String username;
    private final String password;
    private final List<Tweet> tweets;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.tweets = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String pw) {
        return password.equals(pw);
    }

    public void postTweet(Tweet tweet) {
        tweets.add(tweet);
    }

    public List<Tweet> getTweets() {
        return tweets;
    }
}

// Main Twitter Application
public class TwitterAppOOP {
    private static final Scanner sc = new Scanner(System.in);
    private static final Map<String, User> userMap = new HashMap<>();
    private static User currentUser = null;
    private static int tweetIdCounter = 1;
    private static final List<Tweet> allTweets = new ArrayList<>();

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n==== Twitter Console App  ====");
            if (currentUser == null) {
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("0. Exit");
                System.out.print("Choose the number to perform operation : ");
                choice = sc.nextInt(); sc.nextLine();

                switch (choice) {
                    case 1 -> register();
                    case 2 -> login();
                    case 0 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid choice!");
                }
            } else {
                System.out.println("\nHello, " + currentUser.getUsername() + "!");
                System.out.println("1. Post Tweet");
                System.out.println("2. View My Tweets");
                System.out.println("3. View All Tweets");
                System.out.println("4. Like a Tweet");
                System.out.println("5. Top N Recent Tweets");
                System.out.println("6. Tweet Count by Users");
                System.out.println("7. Logout");
                System.out.print("Choose the operation in twitter app: ");
                choice = sc.nextInt(); sc.nextLine();

                switch (choice) {
                    case 1 -> postTweet();
                    case 2 -> viewMyTweets();
                    case 3 -> viewAllTweets();
                    case 4 -> likeTweet();
                    case 5 -> showTopNTweets();
                    case 6 -> showTweetCount();
                    case 7 -> currentUser = null;
                    default -> System.out.println("Invalid choice!");
                }
            }
        } while (true);
    }

    private static void register() {
        System.out.print("Enter username: ");
        String uname = sc.nextLine();
        if (userMap.containsKey(uname)) {
            System.out.println("Username already exists.");
            return;
        }
        System.out.print("Enter password: ");
        String pass = sc.nextLine();
        userMap.put(uname, new User(uname, pass));
        System.out.println("Registered successfully.");
    }

    private static void login() {
        System.out.print("Enter username: ");
        String uname = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();

        User user = userMap.get(uname);
        if (user != null && user.checkPassword(pass)) {
            currentUser = user;
            System.out.println("Login successful.");
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private static void postTweet() {
        System.out.print("Enter tweet: ");
        String content = sc.nextLine();
        Tweet tweet = new Tweet(tweetIdCounter++, content, currentUser.getUsername());
        currentUser.postTweet(tweet);
        allTweets.add(tweet);
        System.out.println("Tweet posted!");
    }

    private static void viewMyTweets() {
        List<Tweet> tweets = currentUser.getTweets();
        if (tweets.isEmpty()) {
            System.out.println("You have no tweets.");
        } else {
            tweets.forEach(Tweet::display);
        }
    }

    private static void viewAllTweets() {
        if (allTweets.isEmpty()) {
            System.out.println("No tweets yet.");
        } else {
            for (Tweet tweet : allTweets) {
                tweet.display();
            }
        }
    }

    private static void likeTweet() {
        System.out.print("Enter Tweet ID to like: ");
        int id = sc.nextInt(); sc.nextLine();
        boolean found = false;
        for (Tweet tweet : allTweets) {
            if (tweet.id == id) {
                tweet.like();
                System.out.println("Liked!");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Tweet not found.");
        }
    }

    private static void showTopNTweets() {
        System.out.print("Enter N (number of recent tweets): ");
        int n = sc.nextInt(); sc.nextLine();
        PriorityQueue<Tweet> pq = new PriorityQueue<>(
            (a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp())
        );
        pq.addAll(allTweets);

        int count = 0;
        System.out.println("Top " + n + " recent tweets:");
        while (!pq.isEmpty() && count < n) {
            pq.poll().display();
            count++;
        }
    }

    private static void showTweetCount() {
        System.out.println("Tweet count per user:");
        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getTweets().size());
        }
    }
}




/*| Concept                 | //Where Used                      |
| ----------------------- | ----------------------------------- |
| **OOP (Encapsulation)** | User class, Tweet class             |
| **OOP (Abstraction)**   | AbstractTweet abstract class        |
| **OOP (Inheritance)**   | Tweet extends AbstractTweet         |
| **OOP (Polymorphism)**  | `display()` method override         |
| **DSA: HashMap**        | For user authentication (`userMap`) |
| **DSA: ArrayList**      | For storing tweets                  |
| **DSA: PriorityQueue**  | For recent tweet feed               |*/
