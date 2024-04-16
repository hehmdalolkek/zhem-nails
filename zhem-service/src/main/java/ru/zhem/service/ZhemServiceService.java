package ru.zhem.service;

import ru.zhem.entity.ZhemService;

import java.util.List;

public interface ZhemServiceService {

    List<ZhemService> findAllServices();

    ZhemService createService(ZhemService service);

    void deleteServiceById(int serviceId);

}
