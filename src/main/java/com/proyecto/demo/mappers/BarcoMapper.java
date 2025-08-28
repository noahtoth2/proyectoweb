<<<<<<< HEAD
package com.proyecto.demo.mappers;

import com.proyecto.demo.dto.BarcoDTO;
import com.proyecto.demo.models.Barco;
=======

package com.proyecto.demo.mappers;


import com.proyecto.demo.dto.BarcoDTO;
import com.proyecto.demo.models.*; 
>>>>>>> c6792885b9bdc81704afc459580f6c7ce3652fd4

public class BarcoMapper {
    public static BarcoDTO toDTO(Barco barco) {
        BarcoDTO barcoDTO = new BarcoDTO();
        barcoDTO.setId(barco.getId());
        barcoDTO.setCedula(barco.getCedula());
        barcoDTO.setFirstName(barco.getFirstName());
        barcoDTO.setLastName(barco.getLastName());
        return barcoDTO;

    }

    public static Barco toEntity(BarcoDTO barcoDTO) {
        Barco barco = new Barco();
        barco.setId(barcoDTO.getId());
        barco.setCedula(barcoDTO.getCedula());
        barco.setFirstName(barcoDTO.getFirstName());
        barco.setLastName(barcoDTO.getLastName());
        return barco;
    }
}