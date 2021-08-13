(ns desafio.model.cliente
  (:require [schema.core :as s]))

(def Cliente
  {:cliente/id    s/Uuid
   :cliente/nome  s/Str
   :cliente/cpf   s/Str
   :cliente/email s/Str
   })


(s/defn cria-novo-cliente :- Cliente
  [nome :- s/Str
   cpf :- s/Str
   email :- s/Str]
  {:cliente/id    (java.util.UUID/randomUUID)
   :cliente/nome  nome
   :cliente/cpf   cpf
   :cliente/email email})

