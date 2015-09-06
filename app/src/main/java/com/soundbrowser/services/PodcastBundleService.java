package com.soundbrowser.services;

import com.j256.ormlite.dao.Dao;
import com.soundbrowser.client.model.Item;
import com.soundbrowser.converters.ItemConverter;
import com.soundbrowser.utils.GeneralUtils;

public class PodcastBundleService {

//	private String baseUrl = "http://192.168.1.80:8080/";
//	private String baseUrl = "http://localhost:8080/";
	private String baseUrl = "http://192.168.1.101:8080/";

	public void buildAndStorePortugueseSamplePodcasts(boolean isToStore, 
		Dao<com.soundbrowser.persistence.model.Item, Integer> daoItem, 
		Dao<com.soundbrowser.persistence.model.Track, Integer> trackDao)
	{
		// Antena 3 podcasts
		String[] podcastsAntena3 = new String[] {
		    "LINHA AVANÇADA",
		    "PROVA ORAL",
		    "PORTUGALEX",
		    "Outra Coisa",
		    "Manhãs da 3",
		    "Não Digo Nomes"
//		    ,"Fora do 5"
		}; 

		final Item geralItem = new Item();
		geralItem.setTitle("Geral");

		final Item rtpItem = new Item();
		rtpItem.setTitle("RTP");
			
		final Item antena3Item = new Item();
		antena3Item.setTitle("Antena 3");
			
		rtpItem.setItens(new Item[] {antena3Item});
		
		buildLeafItens(podcastsAntena3, antena3Item);
		
		// Radio Comercial podcasts
		String[] podcastsRadioComercial = new String[] {
		  "Mixórdia de Temáticas",
		  "O Homem que Mordeu o Cão"
		};

		final Item rcItem = new Item();
		rcItem.setTitle("Radio Comercial");
		
		buildLeafItens(podcastsRadioComercial, rcItem);
		
		geralItem.setItens(new Item[] {rtpItem, rcItem});
		
		// Convert and store in local bd
		com.soundbrowser.persistence.model.Item itemBD = ItemConverter.
			convertFromClientItem2ModelItem(geralItem);
		if(!isToStore)
			GeneralUtils.printRecursiveItem(itemBD);
		else
		{
			try {
				new PodcastService().recursiveBulkSaveItem(
					daoItem, trackDao, itemBD
				);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	private void buildLeafItens(
		String[] podcastsArr, final Item item) 
	{
		Item[] podcastItens = new Item[podcastsArr.length] ;
		int i = 0;
		for (String podcast : podcastsArr) 
		{
			podcastItens[i] = GeneralUtils.buildRestTemplate().getForObject(
				baseUrl + "items/title/" + podcast, Item.class
            );
			i++;
		}
		item.setItens(podcastItens);
	}
		
}
