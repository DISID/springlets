package io.springlets.data.domain.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.data.domain.Sort.Order;

import io.springlets.data.domain.jaxb.SpringletsDataJaxb.OrderDto;

/**
 * JAXB {@link XmlAdapter} to convert Spring Data `Order` instance  
 * into a {@link OrderDto} and vice versa. Useful to be used in JAX-WS
 * SEI interfaces.
 * 
 * Based on the Spring Data's `data.domain.jaxb` classes.
 * 
 * @author Enrique Ruiz at http://www.disid.com[DISID Corporation S.L.]
 */
public class OrderAdapter extends XmlAdapter<OrderDto, Order> {

  public static final OrderAdapter INSTANCE = new OrderAdapter();

  @Override
  public OrderDto marshal(Order order) {

    if (order == null) {
      return null;
    }

    OrderDto dto = new OrderDto();
    dto.direction = order.getDirection();
    dto.property = order.getProperty();
    return dto;
  }

  @Override
  public Order unmarshal(OrderDto source) {
    return source == null ? null : new Order(source.direction, source.property);
  }
}

