package ec.gob.sri.api.catalogo.service.to;

/**
 * 
 */

public class ParametroAmbienteTo {

	private String valor;
	private String ambiente;

	public ParametroAmbienteTo(String valor, String ambiente) {
		super();
		this.valor = valor;
		this.ambiente = ambiente;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

}
