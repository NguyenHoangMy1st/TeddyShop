package com.kin1st.teddybearshopping.repository;

import com.kin1st.teddybearshopping.model.Order;
import com.kin1st.teddybearshopping.model.OrderStatus;
import com.kin1st.teddybearshopping.model.Product;
import com.kin1st.teddybearshopping.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Kiểm tra người dùng đã mua sản phẩm chưa (trạng thái cụ thể)
    boolean existsByUserAndStatusAndItems_Product(User user, OrderStatus status, Product product);

    // Lấy danh sách đơn hàng của người dùng
    List<Order> findByUser(User user);
}
