package com.gmodelo.workservice;

import java.util.ArrayList;
import java.util.List;

import com.gmodelo.beans.AbstractResults;
import com.gmodelo.beans.BurksBean;
import com.gmodelo.beans.HeaderBean;
import com.gmodelo.beans.LgberBean;
import com.gmodelo.beans.LgnumBean;
import com.gmodelo.beans.LgplaBean;
import com.gmodelo.beans.LgtypBean;
import com.gmodelo.beans.LoginBean;
import com.gmodelo.beans.Request;
import com.gmodelo.beans.Response;
import com.gmodelo.beans.WerksBean;
import com.google.gson.Gson;

public class RouteWorkService {

	public String GetRouteService(Request<LoginBean<?>> request) {
		Response<HeaderBean> response = new Response<>();
		String[] almacen = { "LV01", "LV02", "LV03", "LV04" };
		String[] zona = { "A", "B", "C", "D" };
		String zonaStr = "LV";
		String areaStr = "FM";
		HeaderBean header = new HeaderBean();
		BurksBean burks = new BurksBean();
		WerksBean werks = new WerksBean();
		List<WerksBean> werskList = new ArrayList<>();
		burks.setBurks("2013");
		burks.setBurksDesc("Descripcion");
		header.setBurks(burks);
		werks.setWerks("PC13");
		werks.setWerksDesc("Zacatepunk");
		// To-Do Replace this for, XD For query list of Almacen
		List<LgnumBean> lgnumList = new ArrayList<>();
		for (int alm = 0; alm < 4; alm++) {
			LgnumBean lgnum = new LgnumBean();
			lgnum.setLgnum(almacen[alm]);
			lgnum.setLgnumDesc("El Almacen: " + almacen[alm]);
			List<LgtypBean> lgtypList = new ArrayList<>();
			for (int zonaAlm = 0; zonaAlm < 8; zonaAlm++) {
				LgtypBean lgtyp = new LgtypBean();
				lgtyp.setLgtyp(zonaStr + zona[alm] + zonaAlm);
				lgtyp.setLgtypDesc("La zona es: " + zonaStr + zona[alm] + zonaAlm);
				List<LgberBean> lgberList = new ArrayList<>();
				for (int areaZon = 0; areaZon < 4; areaZon++) {
					LgberBean lgber = new LgberBean();
					lgber.setLgber(areaStr + zona[alm] + areaZon);
					lgber.setLgberDesc("La area es: " + areaStr + zona[alm] + areaZon);
					List<LgplaBean> lgplaList = new ArrayList<>();
					for (int carril = 1; carril <= 200; carril++) {
						LgplaBean lgpla = new LgplaBean();
						if (carril > 0 && carril < 10) {
							lgpla.setLgpla(zona[alm] + "000" + carril);
						} else if (carril >= 10 && carril < 100) {
							lgpla.setLgpla(zona[alm] + "00" + carril);
						} else {
							lgpla.setLgpla(zona[alm] + "0" + carril);
						}
						lgplaList.add(lgpla);
					}
					lgber.setLgplaList(lgplaList);
					lgberList.add(lgber);

				}
				lgtyp.setLgberList(lgberList);
				lgtypList.add(lgtyp);

			}
			lgnum.setLgtypList(lgtypList);
			lgnumList.add(lgnum);
		}
		werks.setLgnumList(lgnumList);
		werskList.add(werks);
		burks.setWerskList(werskList);
		response.setLsObject(header);
		response.setAbstractResult(new AbstractResults());
		return new Gson().toJson(response);
	}
}
