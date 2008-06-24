package codalyze.core.entity;

import java.io.Serializable;

import org.hibernate.proxy.HibernateProxyHelper;

public abstract class PersistentObject implements Serializable {
	private static final int LARGE_PRIME = 593;
	protected Long id;
	private Integer version;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		if (id == null) {
			return this.getClass().getName() + ":<transient object>";
		}
		return this.getClass().getName() + ":<" + id.toString() + ">";
	}

	@Override
	public int hashCode() {
		if (id == null) {
			return 0;
		}
		return id.hashCode() * LARGE_PRIME;
	}

	@Override
	public boolean equals(Object other) {

		if (other == null
				|| !(isCompatibleClass(other))) {
			return false;
		}
		if (((PersistentObject) other).getId() == null || getId() == null) {
			return false;
		}
		return ((PersistentObject) other).getId().equals(getId());
	}

	@SuppressWarnings("unchecked")
	private boolean isCompatibleClass(Object other) {
		return HibernateProxyHelper.getClassWithoutInitializingProxy(other).isAssignableFrom(HibernateProxyHelper.getClassWithoutInitializingProxy(this))
				|| HibernateProxyHelper.getClassWithoutInitializingProxy(this).isAssignableFrom(HibernateProxyHelper.getClassWithoutInitializingProxy(other));
	}


	@SuppressWarnings("unchecked")
	public Class getClassWithoutInitializingProxy(){
		return HibernateProxyHelper.getClassWithoutInitializingProxy(this);
	}
	
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getVersion() {
		return version;
	}

}

