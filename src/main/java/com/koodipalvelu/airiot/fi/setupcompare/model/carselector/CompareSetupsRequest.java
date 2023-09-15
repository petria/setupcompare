package com.koodipalvelu.airiot.fi.setupcompare.model.carselector;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompareSetupsRequest {

  private List<IniSections> iniSections;

}
