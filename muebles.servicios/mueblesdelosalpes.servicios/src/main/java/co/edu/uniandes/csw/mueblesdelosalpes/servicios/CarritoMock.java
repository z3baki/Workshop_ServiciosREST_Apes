package co.edu.uniandes.csw.mueblesdelosalpes.servicios;
import co.edu.uniandes.csw.mueblesdelosalpes.dto.Mueble;
import co.edu.uniandes.csw.mueblesdelosalpes.dto.Usuario;
import co.edu.uniandes.csw.mueblesdelosalpes.logica.interfaces.IServicioCarritoMockLocal;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/resource")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CarritoMock {

    /**
     * Referencia al Ejb de carritos encargada de realizar las operaciones del
     * mismo.
     */
    @EJB
    private IServicioCarritoMockLocal carritoService;

    /**
     * Metodo implementando para devolver una lista de objeto(mueble) que
     * representa los articulos en el carrito de compras.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Mueble> getInventario() {
        return carritoService.getInventario();
    }

    /**
     * Metodo implementado para recibir un objeto (mueble), para liego agregar
     * el mueble al carrito.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addItem(Mueble mueble) {
        carritoService.agregarItem(mueble);
    }

    /**
     * Metodo para buscar la referencia del mueble en el carrito, actualizando
     * la cantidad del muele encontrado y utilizando metodos remover, y agregar
     * actualiza el carrito.
     */
    
    @PUT
    @Path("/{referencia}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updteItem(@PathParam("referencia") long referencia, Mueble mueble) {
        Mueble item = findItemByReferencia(referencia);
        item.setCantidad(mueble.getCantidad());
        carritoService.removerItem(item, true);
        carritoService.agregarItem(item);
    }

    /**
     * Metodo implementado para buscar el mueble del carrito por medio del
     * metodo findItemByReferencia, para posrteriomente eleiminar el mueble del
     * carrito.
     */
    @DELETE
    @Path("/{referencia}")
    public void deleteItem(@PathParam("referencia") long referencia) {
        Mueble item = findItemByReferencia(referencia);
        carritoService.removerItem(item, true);
    }

    /**
     * Metodo implementado para recibir un objeto (mueble), para luego agregar
     * el mueble al carrito y actualizar los datos del usuario.
     */
    @POST
    @Path("/comprar")
    @Consumes(MediaType.APPLICATION_JSON)
    public void placeOrder(Usuario usuario) {
        carritoService.comprar(usuario);
    }

    /**
     * Metodo auxiliar que busca un mueble en el carrito por su referencia (id)
     * y lo devueluve en dado caso de no encontrar el muevel regresa un null
     */
    private Mueble findItemByReferencia(long referencia) {
        for (Mueble item : carritoService.getInventario()) {
            if (item.getReferencia() == referencia) {
                return item;
            }
        }
        return null;
    }
}
