package es.upm.grise.profundizacion.order;

import java.util.ArrayList;
import java.util.Collection;

public class Order {

    private Collection<Item> items;

	/*
	 * Constructor
	 */
    public Order() {
        this.items = new ArrayList<>();
    }

	/*
	 * Method to code / test
	 */
    public void addItem(Item item) throws IncorrectItemException {
        // Validar que el precio sea mayor o igual a cero
        if (item.getPrice() < 0) {
            throw new IncorrectItemException("El precio del item debe ser mayor o igual a cero");
        }

        // Validar que la cantidad sea mayor que cero
        if (item.getQuantity() <= 0) {
            throw new IncorrectItemException("La cantidad del item debe ser mayor que cero");
        }

        // Buscar si el producto ya existe en la lista de items
        Item existingItem = null;
        for (Item i : items) {
            if (i.getProduct().getId() == item.getProduct().getId()) {
                // Si el producto existe, verificar si el precio es el mismo
                if (i.getPrice() == item.getPrice()) {
                    existingItem = i;
                    break;
                }
            }
        }

        // Si existe un item con el mismo producto y precio, incrementar la cantidad
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            // Si no existe o el precio es diferente, añadir el nuevo item
            items.add(item);
        }
    }
    
	/*
	 * Setters/getters
	 */
    public Collection<Item> getItems() {
    	
    	return this.items;
    	
    }

}

