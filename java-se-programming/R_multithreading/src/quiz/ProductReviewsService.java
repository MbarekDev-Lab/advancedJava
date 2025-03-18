package quiz;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class ProductReviewsService {

    private final HashMap<Integer, List<String>> productIdToReviews;
    private final ReadWriteLock readWriteLock;  // ReentrantReadWriteLock for handling read/write operations

    // Constructor to initialize the map and lock
    public ProductReviewsService() {
        this.productIdToReviews = new HashMap<>();
        this.readWriteLock = new ReentrantReadWriteLock(); // Initialize the lock
    }

    /**
     * Adds a product ID if not present
     */
    public void addProduct(int productId) {
        Lock lock = getLockForAddProduct();

        lock.lock();

        try {
            if (!productIdToReviews.containsKey(productId)) {
                productIdToReviews.put(productId, new ArrayList<>());
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes a product by ID if present
     */
    public void removeProduct(int productId) {
        Lock lock = getLockForRemoveProduct();

        lock.lock();

        try {
            if (productIdToReviews.containsKey(productId)) {
                productIdToReviews.remove(productId);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Adds a new review to a product
     * @param productId - existing or new product ID
     * @param review - text containing the product review
     */
    public void addProductReview(int productId, String review) {
        Lock lock = getLockForAddProductReview();

        lock.lock();

        try {
            if (!productIdToReviews.containsKey(productId)) {
                productIdToReviews.put(productId, new ArrayList<>());
            }
            productIdToReviews.get(productId).add(review);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns all the reviews for a given product
     */
    public List<String> getAllProductReviews(int productId) {
        Lock lock = getLockForGetAllProductReviews();

        lock.lock();

        try {
            if (productIdToReviews.containsKey(productId)) {
                return Collections.unmodifiableList(productIdToReviews.get(productId));
            }
        } finally {
            lock.unlock();
        }

        return Collections.emptyList();
    }

    /**
     * Returns the latest review for a product by product ID
     */
    public Optional<String> getLatestReview(int productId) {
        Lock lock = getLockForGetLatestReview();

        lock.lock();

        try {
            if (productIdToReviews.containsKey(productId) && !productIdToReviews.get(productId).isEmpty()) {
                List<String> reviews = productIdToReviews.get(productId);
                return Optional.of(reviews.get(reviews.size() - 1));
            }
        } finally {
            lock.unlock();
        }

        return Optional.empty();
    }

    /**
     * Returns all the product IDs that contain reviews
     */
    public Set<Integer> getAllProductIdsWithReviews() {
        Lock lock = getLockForGetAllProductIdsWithReviews();

        lock.lock();

        try {
            Set<Integer> productsWithReviews = new HashSet<>();
            for (Map.Entry<Integer, List<String>> productEntry : productIdToReviews.entrySet()) {
                if (!productEntry.getValue().isEmpty()) {
                    productsWithReviews.add(productEntry.getKey());
                }
            }
            return productsWithReviews;
        } finally {
            lock.unlock();
        }
    }

    /********* LOCKING METHODS ***************/

    // Lock for adding a product
    Lock getLockForAddProduct() {
        // Use write lock because we are modifying the collection
        return readWriteLock.writeLock();
    }

    // Lock for removing a product
    Lock getLockForRemoveProduct() {
        // Use write lock because we are modifying the collection
        return readWriteLock.writeLock();
    }

    // Lock for adding a product review
    Lock getLockForAddProductReview() {
        // Use write lock because we are modifying the collection
        return readWriteLock.writeLock();
    }

    // Lock for retrieving all reviews of a product
    Lock getLockForGetAllProductReviews() {
        // Use read lock because we are only reading from the collection
        return readWriteLock.readLock();
    }

    // Lock for retrieving the latest review of a product
    Lock getLockForGetLatestReview() {
        // Use read lock because we are only reading from the collection
        return readWriteLock.readLock();
    }

    // Lock for retrieving all product IDs with reviews
    Lock getLockForGetAllProductIdsWithReviews() {
        // Use read lock because we are only reading from the collection
        return readWriteLock.readLock();
    }
}


