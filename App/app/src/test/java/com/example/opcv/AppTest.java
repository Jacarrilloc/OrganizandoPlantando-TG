package com.example.opcv;

import android.content.Context;

import com.example.opcv.business.ludification.Level;
import com.example.opcv.model.entity.Address;
import com.example.opcv.model.entity.GardenInfo;
import com.example.opcv.model.entity.User;
import com.example.opcv.model.entity.ValidateRegisterInfo;
import com.example.opcv.model.persistance.repository.local_db.LocalDatabase;

import org.osmdroid.util.GeoPoint;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class AppTest extends TestCase {

    @Mock
    private Context mContext;

    @Test
    public void testCreateInfo() throws IOException {
        String formName = "nombreForm";
        Map<String, Object> infoForm = new HashMap<>();
        infoForm.put("Date", "2023-05-19");
        infoForm.put("CreatedBy", "John Doe");
        infoForm.put("idForm", 1);
        infoForm.put("nameForm",formName);
        String idGarden = "exampleId";

        LocalDatabase info = new LocalDatabase(mContext);
        info.createJsonForm(idGarden, infoForm);
        // Verificar que el método se llamó correctamente
        assertTrue(info.isCreateJsonFormCalled());
    }

    @Test
    public void testDeleteInfo() throws IOException {
        Map<String, Object> infoForm = new HashMap<>();
        infoForm.put("Date", "2023-05-19");
        infoForm.put("CreatedBy", "John Doe");
        infoForm.put("idForm", 1);
        String idGarden = "exampleId";

        LocalDatabase info = new LocalDatabase(mContext);
        info.deleteInfoJson(idGarden, infoForm);
        // Verificar que el método se llamó correctamente
        assertTrue(info.isCreateJsonFormCalled());
    }

    @Test
    public void testUpdateInfo() throws IOException {
        Map<String, Object> infoForm = new HashMap<>();
        infoForm.put("Date", "2023-05-19");
        infoForm.put("CreatedBy", "John Doe");
        infoForm.put("idForm", 1);
        String idGarden = "exampleId";

        LocalDatabase info = new LocalDatabase(mContext);
        info.updateInfoJson(idGarden, infoForm);
        // Verificar que el método se llamó correctamente
        assertTrue(info.isCreateJsonFormCalled());
    }

    @Test
    public void testAddressConstructorAndGetters() {
        // Crear un objeto GeoPoint para usarlo en el test
        double latitude = 37.7749;
        double longitude = -122.4194;
        GeoPoint geoPoint = new GeoPoint(latitude, longitude);

        // Crear una instancia de Address
        String gardenName = "My Garden";
        Address address = new Address(gardenName, geoPoint);

        // Verificar que los valores se hayan asignado correctamente en el constructor
        assertEquals(gardenName, address.getGardenName());
        assertEquals(geoPoint, address.getPoint());
    }

    @Test
    public void testAddressSetters() {
        // Crear una instancia de Address
        Address address = new Address("My Garden", new GeoPoint(0, 0));

        // Cambiar los valores usando los setters
        String newGardenName = "New Garden";
        double newLatitude = 37.7749;
        double newLongitude = -122.4194;
        GeoPoint newGeoPoint = new GeoPoint(newLatitude, newLongitude);

        address.setGardenName(newGardenName);
        address.setPoint(newGeoPoint);

        // Verificar que los valores se hayan actualizado correctamente
        assertEquals(newGardenName, address.getGardenName());
        assertEquals(newGeoPoint, address.getPoint());
    }

    @Test
    public void testGardenInfoConstructorAndGetters() {
        // Crear una instancia de GardenInfo utilizando el constructor con parámetros
        String idOwner = "12345";
        String name = "My Garden";
        String info = "This is my garden";
        String gardenType = "Vegetable";
        String address = "123 Main St";

        GardenInfo gardenInfo = new GardenInfo(idOwner, name, info, gardenType, address);

        // Verificar que los valores se hayan asignado correctamente en el constructor
        assertEquals(idOwner, gardenInfo.getID_Owner());
        assertEquals(name, gardenInfo.getName());
        assertEquals(info, gardenInfo.getInfo());
        assertEquals(gardenType, gardenInfo.getGardenType());
        assertEquals(address, gardenInfo.getAddress());
    }

    @Test
    public void testGardenInfoSetters() {
        // Crear una instancia de GardenInfo utilizando el constructor sin parámetros
        GardenInfo gardenInfo = new GardenInfo();

        // Cambiar los valores utilizando los setters
        String idOwner = "12345";
        String name = "My Garden";
        String info = "This is my garden";
        String gardenType = "Vegetable";
        String address = "123 Main St";

        gardenInfo.setID_Owner(idOwner);
        gardenInfo.setName(name);
        gardenInfo.setInfo(info);
        gardenInfo.setGardenType(gardenType);
        gardenInfo.setAddress(address);

        // Verificar que los valores se hayan actualizado correctamente
        assertEquals(idOwner, gardenInfo.getID_Owner());
        assertEquals(name, gardenInfo.getName());
        assertEquals(info, gardenInfo.getInfo());
        assertEquals(gardenType, gardenInfo.getGardenType());
        assertEquals(address, gardenInfo.getAddress());
    }

    @Test
    public void testUserConstructorAndGetters() {
        // Crear una instancia de User utilizando el constructor con parámetros
        String name = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String id = "123456789";
        String phoneNumber = "1234567890";
        String uriPath = "/path/to/image";
        String gender = "Male";
        int level = 1;

        User user = new User(name, lastName, email, id, phoneNumber, uriPath, gender, level);

        // Verificar que los valores se hayan asignado correctamente en el constructor
        assertEquals(name, user.getName());
        assertEquals(lastName, user.getLastName());
        assertEquals(email, user.getEmail());
        assertEquals(id, user.getId());
        assertEquals(phoneNumber, user.getPhoneNumber());
        assertEquals(uriPath, user.getUriPath());
        assertEquals(gender, user.getGender());
        assertEquals(level, user.getLevel());
    }

    @Test
    public void testUserSetters() {
        // Crear una instancia de User utilizando el constructor sin parámetros
        User user = new User();

        // Cambiar los valores utilizando los setters
        String name = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String id = "123456789";
        String phoneNumber = "1234567890";
        String uriPath = "/path/to/image";
        String gender = "Male";
        int level = 1;

        user.setName(name);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setId(id);
        user.setPhoneNumber(phoneNumber);
        user.setUriPath(uriPath);
        user.setGender(gender);
        user.setLevel(level);

        // Verificar que los valores se hayan actualizado correctamente
        assertEquals(name, user.getName());
        assertEquals(lastName, user.getLastName());
        assertEquals(email, user.getEmail());
        assertEquals(id, user.getId());
        assertEquals(phoneNumber, user.getPhoneNumber());
        assertEquals(uriPath, user.getUriPath());
        assertEquals(gender, user.getGender());
        assertEquals(level, user.getLevel());
    }

    @Test
    public void testUserToMap() {
        // Crear una instancia de User utilizando el constructor con parámetros
        String name = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String id = "123456789";
        String phoneNumber = "1234567890";
        String uriPath = "/path/to/image";
        String gender = "Male";
        int level = 1;

        User user = new User(name, lastName, email, id, phoneNumber, uriPath, gender, level);

        // Obtener el mapa de valores utilizando el método toMap()
        Map<String, Object> map = user.toMap();

        // Verificar que los valores en el mapa sean los esperados
        assertEquals(name, map.get("Name"));
        assertEquals(lastName, map.get("LastName"));
        assertEquals(email, map.get("Email"));
        assertEquals(id, map.get("ID"));
        assertEquals(phoneNumber, map.get("PhoneNumber"));
        assertEquals(uriPath, map.get("UriPath"));
        assertEquals(gender, map.get("Gender"));
        assertEquals(level, map.get("Level"));
    }

    @Test
    public void testValidateFirstRegisterInfo_ValidInfo_ReturnsTrue() {
        // Configurar los valores de entrada válidos
        String name = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        String password = "Password123";
        String confirmPassword = "Password123";
        boolean terms = true;

        // Crear una instancia de ValidateRegisterInfo
        ValidateRegisterInfo validator = new ValidateRegisterInfo();

        // Llamar al método de validación
        boolean result = validator.validateFirstRegisterInfo(name, lastName, email, password, confirmPassword, terms, mContext);

        // Verificar que el resultado sea verdadero
        assertTrue(result);
    }

    @Test
    public void testLevelName_WithInvalidLevel_ReturnsError() {
        // Configurar un nivel inválido
        int level = -1;

        // Crear una instancia de la clase que contiene el método
        Level myClass = new Level();

        // Llamar al método y obtener el resultado
        String result = myClass.levelName(level);

        // Verificar que el resultado sea "Error"
        assertEquals("Error", result);
    }

    @Test
    public void testLevelName_WithValidLevel_ReturnsCorrectName() {
        // Configurar el nivel válido
        int level = 20;

        // Crear una instancia de la clase que contiene el método
        Level myClass = new Level();

        // Llamar al método y obtener el resultado
        String result = myClass.levelName(level);

        // Verificar que el resultado sea el nombre correcto
        assertEquals("Jardinero(a) Novato(a)", result);
    }
}