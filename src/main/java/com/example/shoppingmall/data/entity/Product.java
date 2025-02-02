package com.example.shoppingmall.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Table(name="product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // DB pk

    @Column(nullable = false)
    private String name; // 제품명

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String description; // 제품 설명

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private String imgKey;

    @ManyToOne
    @JoinColumn(name = "user_id") // 다대일 매핑에서 일 쪽의 엔티티에서 참조하는 fk 이름을 적는다
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "product") // 일대다 매핑에서 다 쪽의 어떤 필드를 참조하는지 적는다
    @ToString.Exclude
    private List<Cart> cartList = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<OrderProduct> orderProductList = new ArrayList<>();
}
