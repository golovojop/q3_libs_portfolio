package k.s.yarlykov.libsportfolio.ui

interface IDependencies <out R1, out R2>{
    fun getComponent() : R1
    fun getModule(): R2
}