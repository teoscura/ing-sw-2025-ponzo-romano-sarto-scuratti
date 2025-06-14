package it.polimi.ingsw.view.tui.formatters;

import it.polimi.ingsw.model.client.components.*;
import it.polimi.ingsw.model.components.enums.ComponentRotation;
import it.polimi.ingsw.model.components.enums.ConnectorType;
import it.polimi.ingsw.model.components.enums.ShieldType;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Formatter that builds a string matrix displaying a large {@link ClientComponent} tile.
 */
public class ClientLargeComponentPrinter implements ClientComponentVisitor {

	private final ArrayList<ArrayList<StringBuffer>> component;

	public ClientLargeComponentPrinter() {
		this.component = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			component.add(new ArrayList<>());
			for (int k = 0; k < 3; k++) {
				component.get(i).add(new StringBuffer());
			}
		}
	}

	public void reset() {
		this.component.get(1).get(1).delete(0, this.component.get(1).get(1).length());
		for (ArrayList<StringBuffer> c : this.component) {
			for (StringBuffer s : c) {
				s.delete(0, s.length());
				s.append("..");
			}
		}
	}

	public List<String> getComponentStringsLarge() {
		ArrayList<String> r = new ArrayList<>();
		for (ArrayList<StringBuffer> s : component) {
			StringBuffer str = new StringBuffer();
			for (StringBuffer b : s) {
				str.append(b);
			}
			r.add(str.toString());
		}
		return r;
	}

	public void setCenter(String center) {
		this.component.get(1).get(1).delete(0, this.component.get(1).get(1).length());
		this.component.get(1).get(1).append(center);
	}

	@Override
	public void show(ClientBaseComponent component) {
		this.component.get(0).get(0).delete(0, this.component.get(0).get(0).length());
		this.component.get(0).get(0).append("┌─");
		this.component.get(0).get(1).delete(0, this.component.get(0).get(1).length());
		this.component.get(0).get(1).append(getConnectorSymbol(component.getConnector(ComponentRotation.U000), ComponentRotation.U000));
		this.component.get(0).get(2).delete(0, this.component.get(0).get(2).length());
		this.component.get(0).get(2).append("─┐");


		this.component.get(1).get(0).delete(0, this.component.get(1).get(0).length());
		this.component.get(1).get(0).append(getConnectorSymbol(component.getConnector(ComponentRotation.U270), ComponentRotation.U270));
		this.component.get(1).get(2).delete(0, this.component.get(1).get(2).length());
		this.component.get(1).get(2).append(getConnectorSymbol(component.getConnector(ComponentRotation.U090), ComponentRotation.U090));

		this.component.get(2).get(0).delete(0, this.component.get(2).get(0).length());
		this.component.get(2).get(0).append("└─");
		this.component.get(2).get(1).delete(0, this.component.get(2).get(1).length());
		this.component.get(2).get(1).append(getConnectorSymbol(component.getConnector(ComponentRotation.U180), ComponentRotation.U180));
		this.component.get(2).get(2).delete(0, this.component.get(2).get(2).length());
		this.component.get(2).get(2).append("─┘");
	}

	@Override
	public void show(ClientEmptyComponent component) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				this.component.get(i).get(j).delete(0, this.component.get(i).get(j).length());
				this.component.get(i).get(j).append("..");
			}
		}
	}

	@Override
	public void show(ClientBatteryComponentDecorator component) {
		this.component.get(2).get(0).delete(0, this.component.get(2).get(0).length());
		String s = new AttributedStringBuilder()
				.style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
				.append(String.format("%02d", component.getBatteries()))
				.style(AttributedStyle.DEFAULT).toAttributedString().toAnsi();
		this.component.get(2).get(0).append(s);
	}

	@Override
	public void show(ClientBrokenVerifyComponentDecorator component) {
		this.component.get(0).get(2).delete(0, this.component.get(0).get(2).length());
		this.component.get(0).get(2).append("❌");
	}

	@Override
	public void show(ClientCabinComponentDecorator component) {
		this.component.get(0).get(2).delete(0, this.component.get(0).get(2).length());
		this.component.get(2).get(0).delete(0, this.component.get(2).get(0).length());
		String s = new AttributedStringBuilder()
				.style(AttributedStyle.BOLD.foreground(AttributedStyle.CYAN))
				.append(String.format("%02d", component.getCrew()))
				.style(AttributedStyle.DEFAULT).toAttributedString().toAnsi();
		this.component.get(2).get(0).append(s);
		switch (component.getAlienType()) {
			case HUMAN:
				this.component.get(0).get(2).append("😀");
				break;
			case BROWN:
				this.component.get(0).get(2).append("🐻");
				break;
			case PURPLE:
				this.component.get(0).get(2).append("😈");
				break;
			default:
		}
	}

	@Override
	public void show(ClientCannonComponentDecorator component) {
		switch (component.getRotation()) {
			case U000:
				this.component.get(0).get(1).delete(0, this.component.get(0).get(1).length());
				this.component.get(0).get(1).append("⏫");
				break;
			case U090:
				this.component.get(1).get(2).delete(0, this.component.get(1).get(2).length());
				this.component.get(1).get(2).append("⏩");
				break;
			case U180:
				this.component.get(2).get(1).delete(0, this.component.get(2).get(1).length());
				this.component.get(2).get(1).append("⏬");
				break;
			case U270:
				this.component.get(1).get(0).delete(0, this.component.get(1).get(0).length());
				this.component.get(1).get(0).append("⏪");
				break;
			default:
		}
	}

	@Override
	public void show(ClientEngineComponentDecorator component) {
		switch (component.getRotation()) {
			case U000:
				this.component.get(2).get(1).delete(0, this.component.get(2).get(1).length());
				this.component.get(2).get(1).append("⏫");
				break;
			case U090:
				this.component.get(1).get(0).delete(0, this.component.get(1).get(0).length());
				this.component.get(1).get(0).append("⏩");
				break;
			case U180:
				this.component.get(0).get(1).delete(0, this.component.get(0).get(1).length());
				this.component.get(0).get(1).append("⏬");
				break;
			case U270:
				this.component.get(1).get(2).delete(0, this.component.get(1).get(2).length());
				this.component.get(1).get(2).append("⏪");
				break;
			default:
		}
	}

	@Override
	public void show(ClientLifeSupportComponentDecorator component) {
	}

	@Override
	public void show(ClientPoweredComponentDecorator component) {
		this.component.get(0).get(0).delete(0, this.component.get(0).get(0).length());
		this.component.get(0).get(0).append(component.getPowered() ? "✅" : "❎");
	}

	@Override
	public void show(ClientShieldComponentDecorator component) {
		if (component.getType() != ShieldType.NONE) {
			this.component.get(2).get(0).delete(0, this.component.get(2).get(0).length());
			String s = new AttributedStringBuilder()
					.style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
					.append(component.getType().toString())
					.style(AttributedStyle.DEFAULT).toAttributedString().toAnsi();
			this.component.get(2).get(0).append(s);
		}
	}

	@Override
	public void show(ClientShipmentsComponentDecorator component) {
		String s1 = new AttributedStringBuilder()
				.style(AttributedStyle.BOLD.foreground(AttributedStyle.BLUE))
				.append(String.format("%02d", component.getShipments()[0]))
				.style(AttributedStyle.DEFAULT).toAttributedString().toAnsi();
		String s2 = new AttributedStringBuilder()
				.style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
				.append(String.format("%02d", component.getShipments()[1]))
				.style(AttributedStyle.DEFAULT).toAttributedString().toAnsi();
		String s3 = new AttributedStringBuilder()
				.style(AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
				.append(String.format("%02d", component.getShipments()[2]))
				.style(AttributedStyle.DEFAULT).toAttributedString().toAnsi();
		String s4 = new AttributedStringBuilder()
				.style(AttributedStyle.BOLD.foreground(AttributedStyle.RED))
				.append(String.format("%02d", component.getShipments()[3]))
				.style(AttributedStyle.DEFAULT).toAttributedString().toAnsi();
		this.component.get(0).get(0).delete(0, this.component.get(0).get(0).length());
		this.component.get(0).get(2).delete(0, this.component.get(0).get(2).length());
		this.component.get(2).get(0).delete(0, this.component.get(2).get(0).length());
		this.component.get(2).get(2).delete(0, this.component.get(2).get(2).length());
		this.component.get(0).get(0).append(s1);
		this.component.get(0).get(2).append(s2);
		this.component.get(2).get(0).append(s3);
		this.component.get(2).get(2).append(s4);
	}

	public List<String> getForbidden() {
		List<String> l = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			l.add("🮆🮆🮆🮆🮆🮆");
		}
		return l;
	}

	private String getConnectorSymbol(ConnectorType type, ComponentRotation rotation) {
		switch (type) {
			case DOUBLE_CONNECTOR:
				return "DC";
			case EMPTY:
				return rotation.getShift() % 2 == 0 ? "──" : rotation.getShift() == 3 ? "│ " : " │";
			case SINGLE_CONNECTOR:
				return "SC";
			case UNIVERSAL:
				return "UN";
			default:
				return null;
		}
	}

}
