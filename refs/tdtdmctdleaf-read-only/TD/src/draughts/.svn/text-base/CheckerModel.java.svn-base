package draughts;

import java.io.Serializable;

public class CheckerModel implements Serializable {

	private int i;
	private int j;
	private Sort kind;

	public CheckerModel(int i, int j, Sort kind) {
		super();
		this.i = i;
		this.j = j;
		this.kind = kind;
	}

	public CheckerModel(Checker checkerModel) {
		// TODO Auto-generated constructor stub
		this.i = checkerModel.getI();
		this.j = checkerModel.getJ();
		this.kind = checkerModel.getKind();
	}

	public CheckerModel(CheckerModel checkerModel) {
		// TODO Auto-generated constructor stub
		this.i = checkerModel.getI();
		this.j = checkerModel.getJ();
		this.kind = checkerModel.getKind();
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public Sort getKind() {
		return kind;
	}

	public void setKind(Sort kind) {
		this.kind = kind;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + i;
		result = prime * result + j;
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CheckerModel other = (CheckerModel) obj;
		if (i != other.i)
			return false;
		if (j != other.j)
			return false;
		if (kind == null) {
			if (other.kind != null)
				return false;
		} else if (!kind.equals(other.kind))
			return false;
		return true;
	}

}
