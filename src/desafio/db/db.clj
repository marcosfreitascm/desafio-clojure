(ns desafio.db.db
  (:require [desafio.model.compra :as d.compra]
            [desafio.model.cliente :as d.cliente]
            [desafio.model.cartao :as d.cartao]
            [datomic.api :as d]
            [clojure.walk :as walk])
  (:use clojure.pprint))

(use '[java-time :only (instant)])

(def db-uri "datomic:dev://localhost:4334/desafio")

(defn abre-conexao! []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn apaga-banco! []
  (d/delete-database db-uri))

(def schema-cliente [{:db/ident       :cliente/id
                      :db/valueType   :db.type/uuid
                      :db/cardinality :db.cardinality/one
                      :db/unique      :db.unique/identity}
                     {:db/ident       :cliente/nome
                      :db/valueType   :db.type/string
                      :db/cardinality :db.cardinality/one}
                     {:db/ident       :cliente/cpf
                      :db/valueType   :db.type/string
                      :db/cardinality :db.cardinality/one}
                     {:db/ident       :cliente/email
                      :db/valueType   :db.type/string
                      :db/cardinality :db.cardinality/one}
                     ])

(def schema-cartao [{:db/ident       :cartao/id
                     :db/valueType   :db.type/uuid
                     :db/cardinality :db.cardinality/one
                     :db/unique      :db.unique/identity}
                    {:db/ident       :cartao/numero
                     :db/valueType   :db.type/long
                     :db/cardinality :db.cardinality/one}
                    {:db/ident       :cartao/cvv
                     :db/valueType   :db.type/long
                     :db/cardinality :db.cardinality/one}
                    {:db/ident       :cartao/validade
                     :db/valueType   :db.type/string
                     :db/cardinality :db.cardinality/one}
                    {:db/ident       :cartao/limite
                     :db/valueType   :db.type/bigdec
                     :db/cardinality :db.cardinality/one}
                    {:db/ident       :cartao/cliente
                     :db/valueType   :db.type/ref
                     :db/cardinality :db.cardinality/one}])

(def schema-compra [{:db/ident       :compra/id
                     :db/valueType   :db.type/uuid
                     :db/cardinality :db.cardinality/one
                     :db/unique      :db.unique/identity}
                    {:db/ident       :compra/data
                     :db/valueType   :db.type/instant
                     :db/cardinality :db.cardinality/one}
                    {:db/ident       :compra/valor
                     :db/valueType   :db.type/bigdec
                     :db/cardinality :db.cardinality/one}
                    {:db/ident       :compra/estabelecimento
                     :db/valueType   :db.type/string
                     :db/cardinality :db.cardinality/one}
                    {:db/ident       :compra/categoria
                     :db/valueType   :db.type/string
                     :db/cardinality :db.cardinality/one}
                    {:db/ident       :compra/cartao
                     :db/valueType   :db.type/ref
                     :db/cardinality :db.cardinality/one}])

(defn cria-schema! [conn]
  (mapv #(d/transact conn %) [schema-cliente schema-cartao schema-compra])
  )

(defn snapshot-db [conn]
  (d/db conn))

(defn adiciona-clientes! [conn clientes]
  (d/transact conn clientes))

(defn adiciona-cartoes! [conn cartoes]
  (d/transact conn cartoes))

(defn adiciona-compras! [conn compras]
  (d/transact conn compras))

(defn cria-dados-exemplo [conn]
  (let [cliente1 (d.cliente/cria-novo-cliente
                   "Gabriel Lima"
                   "123456789"
                   "gabriel.lima@email.com")
        cliente2 (d.cliente/cria-novo-cliente
                   "Jose Silva"
                   "44444444444"
                   "jose.silva@email.com")]

    @(adiciona-clientes! conn [cliente1 cliente2])

    (let [cartao1 (d.cartao/cria-novo-cartao
                    2555
                    155
                    "07/28"
                    5000M
                    cliente1)
          cartao2 (d.cartao/cria-novo-cartao
                    2444
                    144
                    "08/29"
                    6000M
                    cliente2)]
      @(adiciona-cartoes! conn [cartao1 cartao2])

      (let [compras [
                     (d.compra/cria-nova-compra
                       (java.util.Date.)
                       1000M
                       "Inova Imobiliaria"
                       "Despesas"
                       cartao2)
                     (d.compra/cria-nova-compra
                       (java.util.Date.)
                       2000M
                       "Frango Frito"
                       "Comida"
                       cartao1)
                     (d.compra/cria-nova-compra
                       (java.util.Date.)
                       20M
                       "Atacadão"
                       "Comida"
                       cartao1)
                     (d.compra/cria-nova-compra
                       (java.util.Date.)
                       40M
                       "Riachuelo"
                       "Roupa"
                       cartao2)
                     (d.compra/cria-nova-compra
                       (java.util.Date.)
                       100M
                       "Renner"
                       "Roupa"
                       cartao1)
                     (d.compra/cria-nova-compra
                       (java.util.Date.)
                       200M
                       "Renner"
                       "Roupa"
                       cartao2)
                     ]]
        @(adiciona-compras! conn compras)
        ))))

(defn dissoc-db-id [entidade]
  (if (map? entidade)
    (dissoc entidade :db/id)
    entidade))

(defn datomic-para-entidade [entidades]
  (walk/prewalk dissoc-db-id entidades))

(defn todos-os-clientes [db]
  (datomic-para-entidade (d/q '[:find [(pull ?cliente [*]) ...]
                                :where [?cliente :cliente/id]]
                              db)))

(defn todas-as-compras [db]
  (datomic-para-entidade (d/q '[:find [(pull ?compra [* {:compra/cartao [* {:cartao/cliente [*]}]}]) ...]
                                :where [?compra :compra/id]]
                              db)))

(defn todos-os-cartoes [db]
  (datomic-para-entidade (d/q '[:find [(pull ?cartao [*]) ...]
                                :where [?cartao :cartao/id]]
                              db)))

(defn obtem-cartao-por-numero [db numero-cartao]
  (datomic-para-entidade (d/q '[:find (pull ?cartao [* {:cartao/cliente [*]}]) .
                                :in $ ?numero
                                :where [?cartao :cartao/numero ?numero]
                                ]
                              db numero-cartao)))

(def cliente1 (d.cliente/cria-novo-cliente
                "Gabriel Lima"
                "123456789"
                "gabriel.lima@email.com"))

(def cliente2 (d.cliente/cria-novo-cliente
                "Jose Silva"
                "44444444444"
                "jose.silva@email.com"))

(def clientes [cliente1 cliente2])

(def cartao1 (d.cartao/cria-novo-cartao
               2555
               155
               "07/28"
               5000
               (:cliente-id cliente1)))

(def cartao2 (d.cartao/cria-novo-cartao
               2444
               144
               "08/29"
               6000
               (:cliente-id cliente2)))






