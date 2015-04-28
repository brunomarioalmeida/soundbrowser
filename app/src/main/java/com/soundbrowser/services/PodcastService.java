package com.soundbrowser.services;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.os.StrictMode;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;
import com.soundbrowser.client.model.Item;
import com.soundbrowser.converters.ItemConverter;
import com.soundbrowser.utils.GeneralUtils;

public class PodcastService {

//	private static String externalUrl = "http://192.168.1.80:8080";
	private static String externalUrl = "http://2.primal-bonbon-627.appspot.com";
//	private static String externalUrl = "http://localhost:8080";
	
	public static com.soundbrowser.persistence.model.Item getItemBaseForPodcastList(com.soundbrowser.persistence.model.Item item) {
		return getItemBaseForPodcastItensList(item).
			getParent().getParent().getItem().get(0).getParent();
	}
	
	public static com.soundbrowser.persistence.model.Item getItemBaseForPodcastItensList(com.soundbrowser.persistence.model.Item item) {
		return getItemWithUrl(item).getParent();
	}
	
//	public static com.soundbrowser.persistence.model.Item getItemBaseForPodcastItensList(
//			com.soundbrowser.persistence.model.Item item) {
//		return getItemWithUrl(item).getParent();
//	}

//	private static com.soundbrowser.persistence.model.Item getItemWithUrl(
//			com.soundbrowser.persistence.model.Item item) {
//		List<com.soundbrowser.persistence.model.Item> itemLst = item.getItem();
//		for (com.soundbrowser.persistence.model.Item child : itemLst) {
//			if(child.getTrack() != null && child.getTrack().getUrl() != null)
//				return child;
//			else
//				return getItemWithUrl(child);
//		}
//		return null;
//	}

	public static com.soundbrowser.persistence.model.Item getItemWithUrl(com.soundbrowser.persistence.model.Item item) 
	{
		List<com.soundbrowser.persistence.model.Item> itemLst = item.getItem();
		for (com.soundbrowser.persistence.model.Item child : itemLst) {
			if(child.getTrack() != null && child.getTrack().getUrl() != null)
				return child;
			else
				return getItemWithUrl(child);
		}
		return null;
	}

//	public static Item recursiveListUp(Item item) 
//	{
//		Item parentItem = item.getParent();
//		if(parentItem.getTitle() != null)
//			return parentItem;
//		else
//			return recursiveListUp(parentItem);
//	}
	
	public static com.soundbrowser.persistence.model.Item recursiveListUp(
			com.soundbrowser.persistence.model.Item item) 
	{
		com.soundbrowser.persistence.model.Item parentItem = item.getParent();
		if(parentItem.getTitle() != null)
			return parentItem;
		else
			return recursiveListUp(parentItem);
	}
	

	public static List<com.soundbrowser.persistence.model.Item> getCreatePodcastList(
		Dao<com.soundbrowser.persistence.model.Item, Integer> daoItem, 
		Dao<com.soundbrowser.persistence.model.Track, ?> daoTrack, 
		String titleStr) 
 	  throws Exception
	{
		PodcastService podcastSrv = new PodcastService();
		
		List<com.soundbrowser.persistence.model.Item> podcastLst = 
			getCurrentList(daoItem, titleStr);
		if(podcastLst == null || podcastLst.isEmpty())
		{
			// Build root path
			podcastSrv.recursiveBulkSaveItem(
				daoItem, daoTrack, 
				GeneralUtils.buildItemPathToRoot()
			);
			
			podcastSrv.saveSubItensToExistingItem(
				daoItem, daoTrack, titleStr
			);
			
			// retry again once the db as been populated
			podcastLst = getCurrentList(daoItem, titleStr);
		}
		return podcastLst;
	};	
	
	public static List<com.soundbrowser.persistence.model.Item> getCurrentList(
		Dao<com.soundbrowser.persistence.model.Item, Integer> daoItem, String titleStr) 
	  throws SQLException 
	{
//		try {

			com.soundbrowser.persistence.model.Item parentItem = daoItem.queryForFirst(
				daoItem.queryBuilder().
					selectColumns("id").
					where().eq("title", titleStr).
				prepare()
			);	
			if(parentItem == null)
				return Collections.EMPTY_LIST;
			
			return daoItem.query(
				daoItem.queryBuilder().
					orderBy("title", true).
					where().eq("parent_id", parentItem.getId()).
				prepare()
			);
//			QueryBuilder<com.soundbrowser.persistence.model.Item, Integer> querySpecificPodcast = 
//				daoItem.queryBuilder();
//			querySpecificPodcast.selectColumns("id");
//			querySpecificPodcast.where().eq("title", titleStr);
//			
//			return daoItem.query(
//				daoItem.queryBuilder().
//					selectColumns("title").distinct().
//					where().in("parent_id", querySpecificPodcast).
//				prepare()
//			);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return Collections.EMPTY_LIST;
	}

	public static List<com.soundbrowser.persistence.model.Item> getCurrentListFromServer(
		String podcastQry) 
	{
        // TODO deal with this later on
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().
			permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(
        	new MappingJacksonHttpMessageConverter()
        );
        com.soundbrowser.client.model.Item item = restTemplate.getForObject(
            externalUrl + "/items/title/" + podcastQry, 
            com.soundbrowser.client.model.Item.class
        );
        Log.i("soundbrowser", "Title - " + item.getTitle());
        Log.i("soundbrowser", "Size  - " + item.getItens().length);

        return ItemConverter.convertFromClientItem2ModelItem(
        	item.getItens()
        );
	}

	public void saveSubItensToExistingItem(
		Dao<com.soundbrowser.persistence.model.Item, Integer> itemDao, 
		Dao<com.soundbrowser.persistence.model.Track, ?> trackDao, 
		String itensFromServerTitle) //, String rootStr) 
	  throws Exception 
	{
		RestTemplate template = new RestTemplate(){{
			getMessageConverters().add(
				new MappingJacksonHttpMessageConverter()
			);
		}};

		com.soundbrowser.client.model.Item item = template.getForObject(
			externalUrl + "/items/title/" + itensFromServerTitle, 
        	com.soundbrowser.client.model.Item.class
        );
	    List<com.soundbrowser.persistence.model.Item> remoteItemLst = 
	    	ItemConverter.convertFromClientItem2ModelItem(item.getItens());

	    Where<com.soundbrowser.persistence.model.Item, ?> whereQuery = 
	    	itemDao.queryBuilder().where().eq("title", itensFromServerTitle); //rootStr);
	    com.soundbrowser.persistence.model.Item subItemRoot = itemDao.query(
			whereQuery.prepare()
		).get(0);
		// TODO protect for non-existing item
    	subItemRoot.setItem(remoteItemLst);

    	itemDao.delete(subItemRoot);  // Avoid repeated item in bd
    	recursiveBulkSaveItem(itemDao, trackDao, subItemRoot);
	}

	public void recursiveBulkSaveItem(
		final Dao<com.soundbrowser.persistence.model.Item, Integer> itemDao, 
		final Dao<com.soundbrowser.persistence.model.Track, ?> trackDao, 
		final com.soundbrowser.persistence.model.Item item) 
	  throws Exception 
	{
		if(item.getTrack() != null)
			item.getTrack().setItem(item);
		itemDao.create(item);
		
		final List<com.soundbrowser.persistence.model.Item> itens = item.getItem();
		if(itens == null || itens.isEmpty())
			return;
		
		itemDao.callBatchTasks(
			new Callable<Void>() {
				public Void call() throws Exception {
					for (final com.soundbrowser.persistence.model.Item i : itens) 
					{
						i.setParent(item);
						if(i.getTrack() != null)
							i.getTrack().setItem(i);

						trackDao.create(i.getTrack());
						recursiveBulkSaveItem(itemDao, trackDao, i);
					}
					return null;
				}
			}
		);
	}
}
