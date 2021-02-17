package me.monkeykiller.survitroll.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TradeWindowManager {

	private Set<TradeWindow> twindows = new HashSet<TradeWindow>();

	public void addTradeWindow(TradeWindow tradeWindow) {
		this.twindows.add(tradeWindow);
	}

	public TradeWindow getTradeById(String TradeId) {
		if (!TradeId.startsWith("survitroll:"))
			TradeId = "survitroll:" + TradeId;
		for (TradeWindow trade : twindows)
			if (trade.getTradeId().equalsIgnoreCase(TradeId))
				return trade;
		return null;
	}

	public ArrayList<String> getIdList() {
		ArrayList<String> tmp = new ArrayList<>();
		for (TradeWindow trade : twindows)
			tmp.add(trade.getTradeId());
		Collections.sort(tmp, String.CASE_INSENSITIVE_ORDER);
		return tmp;
	}
}
