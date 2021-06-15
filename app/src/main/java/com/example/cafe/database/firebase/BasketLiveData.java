package com.example.cafe.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.cafe.models.Basket;
import com.example.cafe.models.BasketProduct;
import com.example.cafe.models.Product;
import com.example.cafe.utilits.constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class BasketLiveData extends MutableLiveData<List<BasketProduct>> {
    private final DatabaseReference mReference;
    private final FirebaseUser mAuth; //Текущий пользователь
    private Query mProduct;     //Получаем все продукты для текущего каталога;
    private final LinkedList<BasketProduct> basketProducts; //Список, в который пушим данные с сервака
    private final String UID; //UID текущего пользователя


    public BasketLiveData() {
        basketProducts = new LinkedList<>();
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        this.mReference = FirebaseDatabase.getInstance().getReference();
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(
                        s -> Log.d(constants.TAG, s)
                );
        UID = (mAuth != null) ? mAuth.getUid() : "";
        mProduct = mReference.child(constants.NODE_BASKET)
                .orderByChild(constants.BASKET_USER).equalTo(UID);
    }


    // callback для узла Корзина
    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
            basketProducts.clear();
            try {
                //Если данные не пусты и не равны null
                if (snapshot.exists()) {
                    //Перебираем все записи таблицы и получаем только для текущего пользователя
                    for (DataSnapshot basket : snapshot.getChildren()) {
                        //Если запись не пуста и значение для модели News существует
                        if (basket.exists() && basket.getValue() != null) {
                            Basket basket_item = basket.getValue(Basket.class);
                            //Устанавливаем callback для этой новости

                            String idProduct = Objects.requireNonNull(basket.getValue(Basket.class)).basket_product;
                            mReference.child(constants.NODE_PRODUCT).child(idProduct).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            try {

                                                Product product = snapshot.getValue(Product.class);
                                                assert basket_item != null;
                                                String count = basket_item.basket_product_count;
                                                assert product != null;
                                                if (Integer.parseInt(count) <= Integer.parseInt(product.product_quantity)) {
                                                    basketProducts.add(new BasketProduct(basket_item, snapshot.getValue(Product.class)));
                                                }

                                                postValue(basketProducts);

                                            } catch (Exception e) {
                                                Log.d(constants.TAG, e.getMessage());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    }
                            );
//                            basket.getKey();
//                            basket.getRef().addValueEventListener(
//                                    new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                                            if (snapshot.exists() && snapshot.getValue() != null) {
//
//                                                try {
//
//                                                    Product product = snapshot.getValue(Product.class);
//                                                    String count = basket_item.basket_product_count;
//                                                    if (Integer.parseInt(count) <= Integer.parseInt(product.product_quantity)) {
//                                                        basketProducts.add(new BasketProduct(basket_item, snapshot.getValue(Product.class)));
//                                                    }
//
//                                                    postValue(basketProducts);
//
//                                                } catch (Exception e) {
//
//                                                }
//
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//                                        }
//                                    }
//                            );
                        }
                    }
                } else {
                    basketProducts.clear();
                    postValue(basketProducts);
                }
            } catch (Exception exception) {
                Log.d(constants.TAG, exception.getMessage());
            }
        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {
            Log.d(constants.TAG, error.getMessage());
        }
    };

    // callback для узла Новости
//    private final ChildEventListener childEventListener = new ChildEventListener() {
//        @Override
//        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
//            //Если данные не пусты и данные не равны null
//            if (snapshot.exists() && snapshot.getValue() != null) {
//
//                try {
//                    String idProduct = snapshot.getValue(Product.class).product_id;
//                    mReference.child(constants.NODE_PRODUCT).orderByChild(constants.PRODUCT_ID).equalTo(idProduct).get().addOnSuccessListener(
//                            new OnSuccessListener<DataSnapshot>() {
//                                @Override
//                                public void onSuccess(DataSnapshot snapshot) {
//                                    String countProduct = snapshot.getValue(Product.class).product_quantity;
//                                }
//                            }
//                    );
//                } catch (Exception e) {
//
//                }
//                basketProducts.add(snapshot.getValue(Product.class));
//                postValue(basketProducts);
//            }
//        }
//
//        @Override
//        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
//            if (snapshot.exists() && snapshot.getValue() != null) {
//                int index = indexOf(basketProducts, snapshot.getKey());
//                //Контролируем выход за пределы границ списка Новостей
//                if (index >= 0 && basketProducts.size() > index)
//                    basketProducts.set(index, snapshot.getValue(Product.class));
//                postValue(basketProducts);
//            }
//        }
//
//        @Override
//        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
//            if (snapshot.exists() && snapshot.getValue() != null) {
//                basketProducts.remove(snapshot.getValue(Product.class));
//                postValue(basketProducts);
//            }
//        }
//
//        @Override
//        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
//
//        }
//
//        @Override
//        public void onCancelled(@NonNull @NotNull DatabaseError error) {
//            Log.d(constants.TAG, error.getMessage());
//        }
//    };

    @Override
    protected void onActive() {
        super.onActive();
        mProduct.addValueEventListener(valueEventListener); //Установка callback для узла НовостиПользователи
    }

    @Override
    protected void onInactive() {
        super.onInactive();
//        mReference.child(constants.NODE_NEWS).removeEventListener(childEventListener); //Удаление callback для узла Новости
        mProduct.removeEventListener(valueEventListener); //Удаление callback для узла НовостиПользователи
    }

    //Utilities

    //получение индекса элемента по news_id
    private static int indexOf(List<Product> list, String id) {
        int index = -1;
        for (Product value : list) {
            index++;
            if (value.product_id.equals(id))
                break;
        }
        return index;
    }

}
