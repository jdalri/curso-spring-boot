package br.com.alura.forum.config.validacao;

// classe que vai representar o erro de algum campo durante a validação
public class ErroFormularioDto {
	private String campo;
	private String erro;
	
	public ErroFormularioDto(String campo, String erro) {
		super();
		this.campo = campo;
		this.erro = erro;
	}

	public String getCampo() {
		return campo;
	}

	public String getErro() {
		return erro;
	}
}
