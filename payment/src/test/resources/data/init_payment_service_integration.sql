INSERT INTO `ecommerce`.`customer`
	(`customer_id`,`name`,`amount`)
VALUES
	(99999, 'TEST_CUSTOMER', 100000);

INSERT INTO `ecommerce`.`category`
	(`category_id`,`name`)
VALUES
	(99999, 'TEST_CATEGORY');

INSERT INTO `ecommerce`.`seller`
	(`seller_id`,`name`)
VALUES
	(99999, 'TEST_SELLER');

INSERT INTO `ecommerce`.`product`
	(`product_id`, `category_id`, `seller_id`, `name`, `sales_price`, `stock`)
VALUES
	(99999, 99999, 99999, 'TEST_PRODUCT', 10, 1000);

INSERT INTO `ecommerce`.`order`
	(`order_id`,`customer_id`,`seller_id`,`product_id`,`quantity`,`sales_price`,`state`)
VALUES
	(99999, 99999, 99999, 99999, 10, 10*10, 0);

INSERT INTO `ecommerce`.`stock_lock`
	(`stock_lock_id`, `product_id`, `order_id`, `sales_price`, `quantity`, `expired_at`)
VALUES
	(99999, 99999, 99999, 10*10, 10, (DATE_ADD(NOW(), INTERVAL 5 MINUTE));

INSERT INTO `ecommerce`.`stock_lock`
    (`stock_lock_id`, `product_id`, `order_id`, `sales_price`, `quantity`, `expired_at`)
VALUES
    (99998, 99999, 99999, 10*10, 10, (DATE_ADD(NOW(), INTERVAL -5 MINUTE));
