namespace models{

using System.Data; 
 using System.Data.SqlClient;

 public class entreStock {


 public int identrestock { get ; set } 


 public Datetime dateEntre { get ; set } 


 public float quantite { get ; set } 


 public double prixUnitaire { get ; set } 


 public double prixTotal { get ; set } 


 public int idmagasin { get ; set } 


 public int idarticle { get ; set } 
}
