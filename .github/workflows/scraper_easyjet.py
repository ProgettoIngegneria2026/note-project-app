from apify_client import ApifyClient
import pandas as pd
from datetime import datetime

# Inserisci il tuo API Token di Apify
client = ApifyClient('apify_api_T9zPJGCBYlXT8MwDBevnfw3I2IcQbo1YayFV')

# Configura i parametri basandoti esattamente sullo screenshot
run_input = {
    "mode": "One-way",
    "arrival": "MXP",
    "departure": "NAP",
    "departureDate": "2026-03-02",
    # Puoi aggiungere "adults": 1 se vuoi specificare il numero di persone
}

# Avvia l'Actor usando l'ID corretto che si vede nell'URL della tua immagine
print("Avvio scraping su EasyJet...")
run = client.actor("wTcFN1RpK4Zbs8rRA").call(run_input=run_input)

# Scarica i risultati
results = []
for item in client.dataset(run["defaultDatasetId"]).iterate_items():
    results.append({
        'timestamp': datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
        'partenza': item.get('departure'),
        'arrivo': item.get('arrival'),
        'data_volo': item.get('departureDate'),
        'prezzo': item.get('price'),
        'compagnia': 'EasyJet'
    })

# Crea il file CSV
df = pd.DataFrame(results)
file_exists = pd.io.common.file_exists('easyjet_prices.csv')
df.to_csv('easyjet_prices.csv', mode='a', index=False, header=not file_exists)

print("Dati salvati in easyjet_prices.csv")