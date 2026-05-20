package ps.eheio.gestionprojetacademique.Repository;

import ps.eheio.gestionprojetacademique.ConnectionDB.ConnectionManager;
import ps.eheio.gestionprojetacademique.Exceptions.ArchiveException;
import ps.eheio.gestionprojetacademique.Exceptions.ConnectionException;
import ps.eheio.gestionprojetacademique.Exceptions.DatabaseException;
import ps.eheio.gestionprojetacademique.model.Eheiannee;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnneeRepository {

    // scanne toutes les BD
    public List<Eheiannee> findAll() {
        List<Eheiannee> annees = new ArrayList<>();

        try {
            Connection conn = ConnectionManager.getServerConnection();

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT schema_name FROM information_schema.schemata " +
                            "WHERE schema_name LIKE 'ehei%'"
            );

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String dbName = rs.getString("schema_name");

               // System.out.println("DB trouvée: " + dbName);

                Eheiannee annee = readMeta(dbName);
                if (annee != null) {
                    annees.add(annee);
                }
            }

            conn.close();

        } catch (SQLException e) {
            System.err.println("Erreur SQL dans findAll()");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur générale dans findAll()");
            e.printStackTrace();
        }

       // System.out.println("Annees trouvées: " + annees.size());

        annees.sort((a, b) -> b.getLibelle().compareTo(a.getLibelle()));
        return annees;
    }
    // lit la table meta d'une BD
    private Eheiannee readMeta(String dbName) {
        try {
            Connection conn = ConnectionManager.getConnection(dbName);
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, libelle, statut FROM meta LIMIT 1"
            );

            ResultSet rs = stmt.executeQuery();

          //  System.out.println("DB testée: " + dbName);

            if (rs.next()) {
                Eheiannee annee = new Eheiannee(
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getString("statut"),
                        dbName
                );

              //  System.out.println("META OK: " + dbName);
                conn.close();
                return annee;
            }

            conn.close();

        } catch (SQLException e) {
            System.err.println("Erreur lecture meta: " + dbName);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur générale: " + dbName);
            e.printStackTrace();
        }

        return null;
    }

    public void archiverEtCreerNouvelle(Eheiannee anneeActive) throws ArchiveException, ConnectionException {
        try {
            // 1 — archiver l'année courante
            Connection        conn = ConnectionManager.getActiveConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE meta SET statut = 'archivé' WHERE id = ?"
            );
            stmt.setInt(1, anneeActive.getId());
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new ArchiveException(
                        "Aucune ligne mise à jour — id introuvable: " + anneeActive.getId()
                );
            }

            // 2 — calculer le nom de la nouvelle BD
            String[] parts      = anneeActive.getLibelle().split("-");
            int      nextEnd    = Integer.parseInt(parts[1]) + 1;
            int      nextStart  = nextEnd - 1;
            String   newLibelle = nextStart + "-" + nextEnd;
            String   newDbName  = "ehei" + nextEnd;
            System.out.println("new db"+newDbName+""+"labelle"+newLibelle);
            // 3 — créer la nouvelle BD
            creerNouvelleBD(newDbName, newLibelle);

            // 4 — basculer la connexion
            ConnectionManager.switchActiveConnection(newDbName);

        } catch (ArchiveException | ConnectionException e) {
            throw e;
        } catch (Exception e) {
            throw new ArchiveException(
                    "Erreur lors de l'archivage de l'année: " + anneeActive.getLibelle(), e
            );
        }
    }

    private void creerNouvelleBD(String dbName, String libelle) throws DatabaseException {
        try {
            // étape 1 — créer la BD
            Connection serverConn = ConnectionManager.getServerConnection();
            Statement  stmt       = serverConn.createStatement();
            stmt.executeUpdate("CREATE DATABASE " + dbName);
            System.out.println("✅ BD créée: " + dbName);
            serverConn.close();


            // étape 2 — se connecter sur la nouvelle BD
            Connection newConn = ConnectionManager.getConnection(dbName);
            System.out.println("✅ Connecté sur nouvelle BD: " + dbName);

            // étape 3 — exécuter le script
            System.out.println("🔄 Début exécution init.sql...");
            executerScript(newConn, libelle);
            System.out.println("✅ Script exécuté");
            newConn.close();

        } catch (ConnectionException e) {
            throw e;
        }catch (Exception e) {
            System.out.println("❌ Erreur dans creerNouvelleBD: " + e.getMessage());
            e.printStackTrace();
            throw new DatabaseException(
                    "Erreur lors de la création de la BD: " + dbName, e
            );
        }
    }
    private void executerScript(Connection conn, String libelle) throws DatabaseException {
        try {
            // chemin absolu vers target/classes
            String path = getClass()
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();

            File sqlFile = new File(path + "ps/eheio/gestionprojetacademique/scriptSQL/init.sql");

            System.out.println("📄 Chemin init.sql: " + sqlFile.getAbsolutePath());
            System.out.println("📄 Fichier existe: " + sqlFile.exists());

            if (!sqlFile.exists()) {
                throw new DatabaseException("init.sql introuvable: " + sqlFile.getAbsolutePath());
            }

            String sql = new String(java.nio.file.Files.readAllBytes(sqlFile.toPath()))
                    .replace("LIBELLE_PLACEHOLDER", libelle);

            Statement stmt = conn.createStatement();
            for (String instruction : sql.split(";")) {
                String trimmed = instruction.trim().replaceAll("\\s+", " ");
                if (!trimmed.isEmpty()) {
                    stmt.executeUpdate(trimmed);
                }
            }

        } catch (DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException(
                    "Erreur exécution init.sql pour: " + libelle, e
            );
        }
    }
   /* private Eheiannee readMeta(String dbName) {
        try {
            Connection conn = ConnectionFactory.getActiveConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT libelle, statut FROM meta LIMIT 1"
            );
            ResultSet rs = stmt.executeQuery();
            System.out.println("DB testée: " + dbName);
            if (rs.next()) {
                return new Eheiannee(
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getString("statut"),
                        dbName
                );
            }System.out.println("META OK: " + dbName);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lecture meta: " + dbName);
        }
        return null;
    }*/

    // liste toutes les années depuis la BD active
    /*public List<Eheiannee> findAll() {
        List<Eheiannee> annees = new ArrayList<>();
        try {
            Connection conn = ConnectionFactory.getActiveConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, libelle, statut FROM meta ORDER BY libelle DESC"
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                annees.add(new Eheiannee(
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getString("statut"),
                        ConnectionFactory.getActiveDbName()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return annees;
    }*/

}
